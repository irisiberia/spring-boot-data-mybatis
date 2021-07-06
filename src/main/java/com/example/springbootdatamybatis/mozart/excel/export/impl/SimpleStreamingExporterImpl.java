package com.example.springbootdatamybatis.mozart.excel.export.impl;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:22
 **/

import com.example.springbootdatamybatis.mozart.NonClosingOutputStream;
import com.example.springbootdatamybatis.mozart.excel.export.ExcelAttrParser;
import com.example.springbootdatamybatis.mozart.excel.export.StreamingExporter;
import com.example.springbootdatamybatis.mozart.excel.export.annotations.ExcelSheet;
import com.example.springbootdatamybatis.mozart.utils.MAssert;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.AreaListener;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 使用Jxls SxssfTransfomer实现的流式导出工具类
 * <p>
 * Created by fan.tang on 2017/6/16.
 */

/**
 * 使用Jxls SxssfTransfomer实现的流式导出工具类
 * <p>
 * Created by fan.tang on 2017/6/16.
 */
public class SimpleStreamingExporterImpl implements StreamingExporter {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStreamingExporterImpl.class);

    private static final String SIMPLE_GRID_TEMPLATE = "simple_export_template.xlsx";
    private static final String RESULT_SHEET_NAME = "Result";
    private static final String DATA_VAR = "data";
    private static final String HEADER_VAR = "headers";

    @Override
    public <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs)
            throws IOException, InvalidFormatException {
        export(dataStream, elementClazz, writeToOs, false);
    }

    @Override
    public <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs,
                           boolean lockResultSheet) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        export(dataStream, elementClazz, writeToOs, lockResultSheet, null, 0);
    }

    @Override
    public <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title)
            throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        export(dataStream, elementClazz, writeToOs, false, title, 0);
    }

    @Override
    public <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title, int sheetRowMaxSize) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        MAssert.isTrue(sheetRowMaxSize > 0, "sheetRowMaxSize必须大于0!");
        export(dataStream, elementClazz, writeToOs, false, title, sheetRowMaxSize);
    }

    private <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs,
                            boolean lockResultSheet, String title, int sheetRowMaxSize) throws IOException, InvalidFormatException {
        MAssert.notNull(dataStream);
        MAssert.notNull(elementClazz);
        MAssert.notNull(writeToOs);
        File file = new File("D:\\blibee\\spring-boot-data-mybatis\\src\\main\\resources\\simple_export_template.xlsx");

        try (

                InputStream is = new FileInputStream(file)) {
            // os外部参数传入，由外部接口
            try (OutputStream os = new NonClosingOutputStream(writeToOs)) {
                Workbook workbook = WorkbookFactory.create(is);
                // SxssfTransformer流式写入
                Transformer transformer = PoiTransformer.createSxssfTransformer(workbook);
                AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
                List<Area> xlsAreaList = areaBuilder.build();
                final Area topArea = xlsAreaList.get(0);

                final String templateSheetName = topArea.getStartCellRef().getSheetName();
                final String customizeSheetName = getCustomizeSheetName(elementClazz);
                final String sheetName = StringUtils.isBlank(customizeSheetName) ? RESULT_SHEET_NAME
                        : customizeSheetName;
                if (sheetRowMaxSize > 0) {//分sheet
                    flushDataToArea(dataStream, elementClazz, topArea, sheetName, title, sheetRowMaxSize);
                } else {
                    flushDataToArea(dataStream, elementClazz, topArea, sheetName, title);
                }
                if (!StringUtils.equals(templateSheetName, sheetName)) {
                    // 删除模板sheet
                    transformer.deleteSheet(templateSheetName);
                }

                final Workbook targetWorkbook = ((PoiTransformer) transformer).getWorkbook();
                if (lockResultSheet) {
                    lockSheets(targetWorkbook);
                }
                targetWorkbook.write(os);
            }
        }
    }

    private void lockSheets(Workbook targetWorkbook) {
        checkNotNull(targetWorkbook);

        targetWorkbook.sheetIterator().forEachRemaining(sh -> sh.protectSheet(StringUtils.EMPTY));
    }

    private <T> String getCustomizeSheetName(Class<T> elementClazz) {
        final ExcelSheet sheet = elementClazz.getAnnotation(ExcelSheet.class);
        if (sheet == null) {
            return StringUtils.EMPTY;
        }

        return sheet.sheetName();
    }

    /**
     * 分sheet时,写title和 header
     *
     * @param context
     * @param topArea
     * @param title
     * @param gridComm
     * @param excelAttrParser
     * @param <T>
     * @return
     */
    private <T> CellRef writeTitleAndHeaderForSheet(final Context context, final Area topArea, final String title,
                                                    final GridCommand gridComm, final ExcelAttrParser<T> excelAttrParser, String sheetName) {
        topArea.reset();
        // 切换sheet
        CellRef sheetCurCell = new CellRef(sheetName + "!A1");
        //写title
        List<String> titleLists = Lists.newArrayList();
        excelAttrParser.getHeaders().forEach(header -> {
            titleLists.add(title);
        });
        context.putVar(HEADER_VAR, titleLists);
        context.putVar(DATA_VAR, Lists.newArrayList());
        sheetCurCell = applyDataToArea(topArea, context, sheetCurCell);

        //写header
        context.putVar(HEADER_VAR, excelAttrParser.getHeaders());
        context.putVar(DATA_VAR, Lists.newArrayList());
        sheetCurCell = applyDataToArea(topArea, context, sheetCurCell);

        gridComm.setHeaders(null);

        return sheetCurCell;
    }

    private <T> void flushDataToArea(Iterable<List<T>> dataStream, Class<T> elementClazz, final Area topArea,
                                     String resultSheetName, String title) {
        final Context context = new Context();
        context.getConfig().setIsFormulaProcessingRequired(false);
        final CellRef startTargetCell = new CellRef(resultSheetName + "!A1");
        CellRef curCell = startTargetCell;
        final ExcelAttrParser<T> excelAttrParser = new ExcelAttrParser<>(elementClazz);
        // 循环开始先写表头
        final GridCommand gridComm = (GridCommand) topArea.getCommandDataList().get(0).getCommand();
        //写title
        if (StringUtils.isNotBlank(title)) {
            curCell = flushTitleDataToArea(title, excelAttrParser, topArea, context, startTargetCell);
        }
        //写header
        context.putVar(HEADER_VAR, excelAttrParser.getHeaders());
        context.putVar(DATA_VAR, Lists.newArrayList());
        curCell = applyDataToArea(topArea, context, curCell);

        // 循环导出结果时不写表头
        gridComm.setHeaders(null);

        final Map<Integer, String> colCellStyles = excelAttrParser.colCellStyles();
        final Workbook workbook = ((PoiTransformer) topArea.getTransformer()).getWorkbook();
        final LoadingCache<Integer, Pair<CellStyle, DataFormat>> cache = createCache(workbook);
        if (MapUtils.isNotEmpty(colCellStyles)) {
            final Area bodyArea = gridComm.getAreaList().get(1);
            bodyArea.addAreaListener(new GridBodyAreaListener(workbook, colCellStyles, cache));
        }
        // 迭代地将每批数据写入Sheet
        for (List<T> data : dataStream) {
            context.putVar(DATA_VAR,
                    data.stream().map(excelAttrParser::propertyValuesList).collect(Collectors.toList()));
            curCell = applyDataToArea(topArea, context, curCell);
        }
    }

    private <T> void flushDataToArea(Iterable<List<T>> dataStream, Class<T> elementClazz, final Area topArea,
                                     String resultSheetName, String title, int sheetRowMaxSize) {
        final Context context = new Context();
        context.getConfig().setIsFormulaProcessingRequired(false);
        final CellRef startTargetCell = new CellRef(resultSheetName + "!A1");
        CellRef curCell = startTargetCell;
        final ExcelAttrParser<T> excelAttrParser = new ExcelAttrParser<>(elementClazz);
        // 循环开始先写表头
        final GridCommand gridComm = (GridCommand) topArea.getCommandDataList().get(0).getCommand();
        // 循环导出结果时不写表头
        final String headers = gridComm.getHeaders();
        //写title
        if (StringUtils.isNotBlank(title)) {
            curCell = flushTitleDataToArea(title, excelAttrParser, topArea, context, startTargetCell);
        }
        //写header
        context.putVar(HEADER_VAR, excelAttrParser.getHeaders());
        context.putVar(DATA_VAR, Lists.newArrayList());
        curCell = applyDataToArea(topArea, context, curCell);
        gridComm.setHeaders(null);

        final Map<Integer, String> colCellStyles = excelAttrParser.colCellStyles();
        final Workbook workbook = ((PoiTransformer) topArea.getTransformer()).getWorkbook();
        final LoadingCache<Integer, Pair<CellStyle, DataFormat>> cache = createCache(workbook);
        if (MapUtils.isNotEmpty(colCellStyles)) {
            final Area bodyArea = gridComm.getAreaList().get(1);
            bodyArea.addAreaListener(new GridBodyAreaListener(workbook, colCellStyles, cache));
        }

        int currentSheetRowSize = 0;
        int currentSheetSize = 1;
        boolean isAddRow = false;
        // 迭代地将每批数据写入Sheet
        for (List<T> data : dataStream) {
            int size = data.size();
            /**
             * 当前sheet页需要的条数< data数量 则需要新建sheet
             */
            int needSheetRowSize = sheetRowMaxSize - currentSheetRowSize;
            if (isAddRow && needSheetRowSize < size) {
                if (needSheetRowSize > 0) {
                    context.putVar(DATA_VAR, data.subList(0, needSheetRowSize).stream()
                            .map(excelAttrParser::propertyValuesList).collect(Collectors.toList()));
                    applyDataToArea(topArea, context, curCell);
                    gridComm.setHeaders(headers);
                }
                // 切换sheet
                String sheetName = resultSheetName + currentSheetSize;
                curCell = writeTitleAndHeaderForSheet(context, topArea, title,
                        gridComm, excelAttrParser, sheetName);
                currentSheetSize++;
                currentSheetRowSize = 0;
                isAddRow = false;
            }

            /**
             * 按照sheetRowMaxSize 来分sheet
             */
            List<List<T>> sheetDataList;
            if (needSheetRowSize > 0 && needSheetRowSize < size && needSheetRowSize < sheetRowMaxSize) {
                sheetDataList = Lists.partition(data.subList(needSheetRowSize, size), sheetRowMaxSize);
            } else {
                sheetDataList = Lists.partition(data, sheetRowMaxSize);
            }
            int lastSheetRowSize = 0;
            for (List<T> sheetData : sheetDataList) {
                int sheetSize = sheetData.size();
                context.putVar(DATA_VAR,
                        sheetData.stream().map(excelAttrParser::propertyValuesList).collect(Collectors.toList()));
                curCell = applyDataToArea(topArea, context, curCell);
                if (sheetSize == sheetRowMaxSize) {
                    gridComm.setHeaders(headers);
                    // 切换sheet
                    String sheetName = resultSheetName + currentSheetSize;
                    curCell = writeTitleAndHeaderForSheet(context, topArea, title,
                            gridComm, excelAttrParser, sheetName);
                    currentSheetSize++;
                    currentSheetRowSize = 0;
                    isAddRow = false;
                    continue;
                }
                isAddRow = true;
                lastSheetRowSize = sheetSize;
            }
            currentSheetRowSize += lastSheetRowSize;
        }
        /**
         * 删掉最后一个只有header的sheet
         */
        if (workbook.getSheetAt(currentSheetSize).getLastRowNum() == 1) {
            workbook.removeSheetAt(currentSheetSize);
        }
    }

    // 如果需要指定单元格格式的话每一列对应一个新对象, 对象缓存10分钟
    private LoadingCache<Integer, Pair<CellStyle, DataFormat>> createCache(final Workbook workbook) {
        return CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, Pair<CellStyle, DataFormat>>() {
                    @Override
                    public Pair<CellStyle, DataFormat> load(Integer key) throws Exception {
                        final CellStyle newStyle = workbook.createCellStyle();
                        final DataFormat dataFormat = workbook.createDataFormat();

                        return Pair.of(newStyle, dataFormat);
                    }
                });
    }

    /**
     * 写title
     *
     * @param title
     * @param excelAttrParser
     * @param topArea
     * @param context
     * @param startTargetCell
     * @param <T>
     * @return
     */
    private <T> CellRef flushTitleDataToArea(final String title, final ExcelAttrParser<T> excelAttrParser,
                                             final Area topArea, final Context context, final CellRef startTargetCell) {
        // 循环开始先写表头
        final GridCommand gridComm = (GridCommand) topArea.getCommandDataList().get(0).getCommand();
        int col = excelAttrParser.getHeaders().size();
        int row = 0;
        gridComm.getAreaList().get(0).addAreaListener(
                new TitleBodyAreaListener(((PoiTransformer) topArea.getTransformer()).getWorkbook(), col, row));
        List<String> titleLists = Lists.newArrayList();
        excelAttrParser.getHeaders().forEach(header -> {
            titleLists.add(title);
        });
        context.putVar(HEADER_VAR, titleLists);
        context.putVar(DATA_VAR, Lists.newArrayList());
        return applyDataToArea(topArea, context, startTargetCell);
    }

    private CellRef applyDataToArea(Area topArea, Context context, CellRef curCell) {
        Size size = topArea.applyAt(curCell, context);
        CellRef nextCell = new CellRef(curCell.getSheetName(), curCell.getRow() + size.getHeight(), 0);
        return nextCell;
    }

    static class TitleBodyAreaListener implements AreaListener {
        Workbook workbook;
        int col = 0;
        int row = 0;

        public TitleBodyAreaListener(Workbook workbook, int col, int row) {
            this.workbook = workbook;
            this.col = col - 1;
            this.row = row;
        }

        @Override
        public void beforeApplyAtCell(CellRef cellRef, Context context) {

        }

        @Override
        public void afterApplyAtCell(CellRef cellRef, Context context) {
            if (cellRef.getCol() == col && cellRef.getRow() == row) {
                final Sheet sheet = workbook.getSheet(cellRef.getSheetName());
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row, row, 0, col);
                sheet.addMergedRegion(cellRangeAddress);
                final Cell tc = sheet.getRow(row).getCell(0);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                tc.setCellStyle(cellStyle);
            }
        }

        @Override
        public void beforeTransformCell(CellRef srcCell, CellRef targetCell, Context context) {

        }

        @Override
        public void afterTransformCell(CellRef srcCell, CellRef targetCell, Context context) {

        }
    }

    static class GridBodyAreaListener implements AreaListener {

        Workbook workbook;

        Map<Integer, String> colCellStyles;

        LoadingCache<Integer, Pair<CellStyle, DataFormat>> cache;

        public GridBodyAreaListener(Workbook workbook, Map<Integer, String> colCellStyles,
                                    LoadingCache<Integer, Pair<CellStyle, DataFormat>> cache) {
            this.workbook = workbook;
            this.colCellStyles = colCellStyles;
            this.cache = cache;
        }

        @Override
        public void beforeApplyAtCell(CellRef cellRef, Context context) {

        }

        @Override
        public void afterApplyAtCell(CellRef cellRef, Context context) {

        }

        @Override
        public void beforeTransformCell(CellRef srcCell, CellRef targetCell, Context context) {

        }

        @Override
        public void afterTransformCell(CellRef srcCell, CellRef targetCell, Context context) {

            final int targetCellCol = targetCell.getCol();

            final String cellStyle = colCellStyles.getOrDefault(targetCellCol, StringUtils.EMPTY);
            Pair<CellStyle, DataFormat> cellStyleDataFormatPair = null;
            try {
                cellStyleDataFormatPair = cache.get(targetCellCol);
            } catch (ExecutionException e) {

            }
            if (StringUtils.isNotBlank(cellStyle) && cellStyleDataFormatPair != null) {
                final Sheet sheet = workbook.getSheet(targetCell.getSheetName());
                final Cell tc = sheet.getRow(targetCell.getRow()).getCell(targetCellCol);

                final CellStyle tcs = tc.getCellStyle();
                final CellStyle newTcs = cellStyleDataFormatPair.getKey();
                // new style和 old style来源于同一个workbook, 内部拷贝的时候只会执行get,set 没有额外的copy开销
                newTcs.cloneStyleFrom(tcs);
                newTcs.setDataFormat(cellStyleDataFormatPair.getRight().getFormat(cellStyle));
                tc.setCellStyle(newTcs);
            }
        }
    }




    public static void main(String[] args) throws IOException, InvalidFormatException {
                 File file = new File("D:\\wewe.xlsx");



                InputStream is = new FileInputStream(file);
            // os外部参数传入，由外部接口

                Workbook workbook = WorkbookFactory.create(is);
                // SxssfTransformer流式写入
                Transformer transformer = PoiTransformer.createSxssfTransformer(workbook);
                AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
                List<Area> xlsAreaList = areaBuilder.build();
                final Area topArea = xlsAreaList.get(0);
        System.out.println("ewew");
    }

}
