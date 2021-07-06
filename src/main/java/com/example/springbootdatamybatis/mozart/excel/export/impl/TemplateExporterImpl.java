package com.example.springbootdatamybatis.mozart.excel.export.impl;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:22
 **/

import com.example.springbootdatamybatis.mozart.NonClosingOutputStream;
import com.example.springbootdatamybatis.mozart.excel.beans.Context;
import com.example.springbootdatamybatis.mozart.excel.export.TemplateExporter;
import com.example.springbootdatamybatis.mozart.excel.export.command.HideColumnsCommand;
import com.example.springbootdatamybatis.mozart.excel.export.command.PrintAreaCommand;
import com.example.springbootdatamybatis.mozart.excel.export.command.PrintTitleCommand;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.EachCommand;
import org.jxls.command.MergeCellsCommand;
import org.jxls.common.CellRef;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.CellRefUtil;
import org.jxls.util.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 模板导出类jxls实现
 *
 * Created by fan.tang on 2017/6/17.
 */
public class TemplateExporterImpl implements TemplateExporter {

    private static final String RESULT_SHEET_NAME = "Result";
    private static final String SHEET_COLS_SEPARATOR = "!";

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateExporterImpl.class);

    @Override
    public void export(Context context, OutputStream os) throws IOException {
        org.jxls.common.Context jxlContext = new org.jxls.common.Context();
        InputStream templateIs = context.getTemplateInputStream();

        if (MapUtils.isNotEmpty(context.getTemplateValueMap())) {
            context.getTemplateValueMap().entrySet()
                    .forEach(entry -> jxlContext.putVar(entry.getKey(), entry.getValue()));
        }

        if (MapUtils.isNotEmpty(context.getCellDataUpdaterMap())) {
            context.getCellDataUpdaterMap().entrySet()
                    .forEach(entry -> jxlContext.putVar(entry.getKey(), entry.getValue()));
        }

        Transformer transformer = TransformerFactory.createTransformer(templateIs, new NonClosingOutputStream(os));
        final Workbook workbook = ((PoiTransformer) transformer).getWorkbook();
        //模版样式
        final Map<String, FormatContext> formatContextMap = Maps.newHashMap();
        for ( int i = 0; i < workbook.getNumberOfSheets(); i++) {
            FormatContext formatContext = FormatContext.create(workbook.getSheetAt(i));
            formatContextMap.put(workbook.getSheetAt(i).getSheetName(),formatContext);
        }
        AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);

        List<Area> xlsAreaList = areaBuilder.build();

        for(Area area : xlsAreaList) {
            CellRef templateStartCellRef = area.getStartCellRef();
            String cellRef =  getResultStartRef(templateStartCellRef);
            area.applyAt(new CellRef(cellRef), jxlContext);
            if(CollectionUtils.isNotEmpty(area.getCommandDataList())) {
                Object command = area.getCommandDataList().get(0).getCommand();
                if(!(command instanceof EachCommand)) {
                    continue;
                }
                EachCommand eachCommand = (EachCommand) command;
                if (StringUtils.isNotEmpty(eachCommand.getMultisheet())) {
                    List<String> sheetNames = (List<String>) jxlContext.getVar(eachCommand.getMultisheet());
                    FormatContext formatContext = formatContextMap.get(templateStartCellRef.getSheetName());
                    sheetNames.stream().forEach(o -> formatContextMap.put(o, formatContext));

                    //删除拥有multisheet命令的模版sheet
                    transformer.deleteSheet(templateStartCellRef.getSheetName());
                    formatContextMap.remove(templateStartCellRef.getSheetName());
                }
            }
        }

        // 拷贝模板sheet的格式
        keepTemplateFormat(workbook, formatContextMap);
        // 填充完数据后处理sheet数据的回调processor
        postProcessSheets(workbook, context);
        // 保护sheet页
        lockSheets(workbook, context);

        transformer.write();
    }

    private void postProcessSheets(Workbook workbook, Context context) {
        if (workbook == null || CollectionUtils.isEmpty(context.getAfterProcessors())) {
            LOGGER.debug("没有注册任何处理器，跳过执行");
            return;
        }

        workbook.sheetIterator()
                .forEachRemaining(sh -> context.getAfterProcessors().stream().forEach(p -> p.postProcess(sh)));
    }


    private void lockSheets(Workbook workbook, Context context) {
        checkNotNull(workbook);
        checkNotNull(context);

        final Set<String> sheetNamesToLock = context.getSheetNamesToLock();
        final Set<Integer> sheetIndexToLock = context.getSheetIndexToLock();
        if (CollectionUtils.isNotEmpty(sheetNamesToLock) || CollectionUtils.isNotEmpty(sheetIndexToLock)) {
            workbook.sheetIterator().forEachRemaining(sh -> {
                final boolean needLock = sheetNamesToLock.contains(sh.getSheetName())
                        || sheetIndexToLock.contains(workbook.getSheetIndex(sh));
                if (needLock) {
                    sh.protectSheet(StringUtils.EMPTY);
                }
            });
        }
    }

    static {
        // 自定义合并单元格命令
        XlsCommentAreaBuilder.addCommandMapping("mergeCells", MergeCellsCommand.class);
        // 自定义列隐藏命令
        XlsCommentAreaBuilder.addCommandMapping("hideColumns", HideColumnsCommand.class);
        // 自定义打印区域
        XlsCommentAreaBuilder.addCommandMapping("dynamicPrintTitle", PrintTitleCommand.class);
        // 自定义打印标题
        XlsCommentAreaBuilder.addCommandMapping("dynamicPrintArea", PrintAreaCommand.class);
    }

    private void keepTemplateFormat(Workbook workbook, Map<String, FormatContext> formatContextMap) {
        final int numberOfSheets = workbook.getNumberOfSheets();


        for (int i = 0; i < numberOfSheets; i++) {
            final Sheet sheetAt = workbook.getSheetAt(i);
            final Map<Short, Double> marginsMap = formatContextMap.get(sheetAt.getSheetName()).getMarginMap();
            FormatContext context = formatContextMap.get(sheetAt.getSheetName());
            if (sheetAt != null) {
                // 设置页边距
                for (Map.Entry<Short, Double> entry : marginsMap.entrySet()) {
                    sheetAt.setMargin(entry.getKey(), entry.getValue());
                }

                // 设置页眉
                if (context.getHeader() != null) {
                    sheetAt.getHeader().setCenter(context.getHeader().getCenter());
                    sheetAt.getHeader().setLeft(context.getHeader().getLeft());
                    sheetAt.getHeader().setRight(context.getHeader().getRight());
                }

                // 设置页脚
                if (context.getFooter() != null) {
                    sheetAt.getFooter().setCenter(context.getFooter().getCenter());
                    sheetAt.getFooter().setLeft(context.getFooter().getLeft());
                    sheetAt.getFooter().setRight(context.getFooter().getRight());
                }

                final PrintSetup templatePrintSetUp = context.getPrintSetup();
                if (templatePrintSetUp != null) {
                    final PrintSetup printSetup = sheetAt.getPrintSetup();
                    copySetUp(templatePrintSetUp, printSetup);
                }

                //设置页面内容居中
                sheetAt.setVerticallyCenter(context.verticallyCenter);
                sheetAt.setHorizontallyCenter(context.horizontallyCenter);
            }
        }
    }

    private void copySetUp(PrintSetup from, PrintSetup to) {
        if (from == null || to == null) {
            return;
        }
        to.setCopies(from.getCopies());
        to.setDraft(from.getDraft());
        to.setFitHeight(from.getFitHeight());
        to.setFitWidth(from.getFitWidth());
        to.setFooterMargin(from.getFooterMargin());
        to.setHeaderMargin(from.getHeaderMargin());
        to.setHResolution(from.getHResolution());
        to.setLandscape(from.getLandscape());
        to.setLeftToRight(from.getLeftToRight());
        to.setNoColor(from.getNoColor());
        to.setNoOrientation(from.getNoOrientation());
        to.setNotes(from.getNotes());
        to.setPageStart(from.getPageStart());
        to.setPaperSize(from.getPaperSize());
        to.setScale(from.getScale());
        to.setUsePage(from.getUsePage());
        to.setValidSettings(from.getValidSettings());
        to.setHResolution(from.getHResolution());
        to.setVResolution(from.getVResolution());
    }

    private String getResultStartRef(CellRef templateStartCellRef) {
        checkArgument(templateStartCellRef != null);
        return templateStartCellRef.getSheetName() + SHEET_COLS_SEPARATOR
                + CellRefUtil.convertNumToColString(templateStartCellRef.getCol())
                // excel 把0th行当做第一行
                + String.valueOf(templateStartCellRef.getRow() + 1);
    }

    // 解析目标sheet页的格式上下文，用于保留模板格式
    private static class FormatContext {
        final Map<Short, Double> marginMap = Maps.newHashMap();
        Header header;
        Footer footer;
        PrintSetup printSetup;
        boolean verticallyCenter;
        boolean horizontallyCenter;
        public Map<Short, Double> getMarginMap() {
            return marginMap;
        }

        public Header getHeader() {
            return header;
        }

        public Footer getFooter() {
            return footer;
        }

        public PrintSetup getPrintSetup() {
            return printSetup;
        }

        static FormatContext create(Sheet templateSheet) {
            FormatContext formatContext = new FormatContext();

            final Map<Short, Double> shortDoubleMap = extractMarginsMap(templateSheet);
            if (MapUtils.isNotEmpty(shortDoubleMap)) {
                formatContext.marginMap.putAll(shortDoubleMap);
            }

            formatContext.header = templateSheet.getHeader();
            formatContext.footer = templateSheet.getFooter();
            formatContext.printSetup = templateSheet.getPrintSetup();
            formatContext.horizontallyCenter = templateSheet.getHorizontallyCenter();
            formatContext.verticallyCenter = templateSheet.getVerticallyCenter();
            return formatContext;
        }

        private static Map<Short, Double> extractMarginsMap(Sheet templateSheet) {
            if (templateSheet == null) {
                return MapUtils.EMPTY_MAP;
            }

            final Map<Short, Double> marginMap = Maps.newHashMap();
            marginMap.put(Sheet.LeftMargin, templateSheet.getMargin(Sheet.LeftMargin));
            marginMap.put(Sheet.RightMargin, templateSheet.getMargin(Sheet.RightMargin));
            marginMap.put(Sheet.TopMargin, templateSheet.getMargin(Sheet.TopMargin));
            marginMap.put(Sheet.BottomMargin, templateSheet.getMargin(Sheet.BottomMargin));
            marginMap.put(Sheet.HeaderMargin, templateSheet.getMargin(Sheet.HeaderMargin));
            marginMap.put(Sheet.FooterMargin, templateSheet.getMargin(Sheet.FooterMargin));

            return marginMap;
        }
    }
}
