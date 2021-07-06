package com.example.springbootdatamybatis.mozart.excel.export.command;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:20
 **/

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.JxlsException;
import org.jxls.common.Size;
import org.jxls.expression.ExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 目前只支持明确指定合并范围的单元格合并
 *
 * Created by fan.tang on 2017/6/26.
 */
public class MergeCellsCommand extends AbstractCommand {

    private Logger logger = LoggerFactory.getLogger(MergeCellsCommand.class);

    Area area;

    /**
     * 要合并的列数, 从当前列往右数(包含当前列)
     */
    String cols;

    /**
     * 要合并的行数，从当前行往下数(包含当前行)
     */
    String rows;

    /**
     * 动态列
     */
    String dynamicCols;

    /**
     * 合并后的单元格边框配置，逗号分隔的配置
     *
     * <br/>
     * 边框类型:线条类型 ({@link org.apache.poi.ss.usermodel.BorderStyle})<br/>
     *
     * top:1,bottom:1,left:1,right:0 --> 上下左细线边框，右无边框
     */
    String borderConfigs;

    private static final Splitter.MapSplitter MAP_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults()
            .withKeyValueSeparator(":");

    // 边框样式
    private static final String TOP_BOARDER_TYPE = "top";

    private static final String BOTTOM_BOARDER_TYPE = "bottom";

    private static final String LEFT_BOARDER_TYPE = "left";

    private static final String RIGHT_BOARDER_TYPE = "right";

    private static final Map<String, BorderTypeProcessor> TYPE_PROCESSORS = Maps.newHashMap();

    static {
        TYPE_PROCESSORS.put(TOP_BOARDER_TYPE, (style, region, sheet) -> {
            checkNotNull(style);
            checkNotNull(region);
            checkNotNull(sheet);

            RegionUtil.setBorderTop(style, region, sheet);
        });

        TYPE_PROCESSORS.put(BOTTOM_BOARDER_TYPE, (style, region, sheet) -> {
            checkNotNull(style);
            checkNotNull(region);
            checkNotNull(sheet);

            RegionUtil.setBorderBottom(style, region, sheet);
        });

        TYPE_PROCESSORS.put(LEFT_BOARDER_TYPE, (style, region, sheet) -> {
            checkNotNull(style);
            checkNotNull(region);
            checkNotNull(sheet);

            RegionUtil.setBorderLeft(style, region, sheet);
        });


        TYPE_PROCESSORS.put(RIGHT_BOARDER_TYPE, (style, region, sheet) -> {
            checkNotNull(style);
            checkNotNull(region);
            checkNotNull(sheet);

            RegionUtil.setBorderRight(style, region, sheet);
        });
    }

    private Map<String, String> boarderConfigMaps = Maps.newHashMap();

    @Override
    public String getName() {
        return "mergeCells";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Size resultSize = area.applyAt(cellRef, context);
        if (resultSize.equals(Size.ZERO_SIZE))
            return resultSize;
        PoiTransformer transformer = (PoiTransformer) area.getTransformer();
        Workbook workbook = transformer.getWorkbook();
        Sheet sheet = workbook.getSheet(cellRef.getSheetName());

        final Integer cols = getIntValue(this.cols, context);
        final Integer rows = getIntValue(this.rows, context);

        // 列数不足，需要动态创建列数，针对只合并一行，内容一致的特殊处理
        if (StringUtils.isNotEmpty(dynamicCols) && StringUtils.equalsIgnoreCase(dynamicCols, "true")) {
            return processDynamicCols(cellRef, resultSize, sheet, cols, rows);
        } else {
            final CellRangeAddress region = new CellRangeAddress(cellRef.getRow(), cellRef.getRow() + rows - 1, cellRef.getCol(),
                    cellRef.getCol() + cols - 1);
            sheet.addMergedRegion(region);
            formatMergedRegion(region, sheet);
            return resultSize;
        }
    }

    private void formatMergedRegion(CellRangeAddress region, Sheet sheet) {
        checkNotNull(region, "region不能为null");
        checkNotNull(sheet, "sheet不能为null");

        if (MapUtils.isEmpty(boarderConfigMaps)) {
            return;
        }

        for (Map.Entry<String, String> entry : boarderConfigMaps.entrySet()) {
            final String borderType = entry.getKey();
            final String lineType = entry.getValue();

            final BorderTypeProcessor processor = TYPE_PROCESSORS.get(borderType);
            if (processor == null) {
                logger.warn("无法识别的边框类型, border={}", borderType);
                continue;
            }

            processor.process(BorderStyle.valueOf(Short.valueOf(lineType)), region, sheet);
        }
    }

    private Size processDynamicCols(CellRef cellRef, Size resultSize, Sheet sheet, Integer cols, Integer rows) {
        if (cols > 1) {
            Cell currentCell = sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol());
            // 当前列内容
            boolean needMerge = true;
            for (int i = 1; i < cols; i++) {
                if (cellRef.getCol() - i < 0) {
                    needMerge = false;
                    break;
                }
                if (!compareValue(currentCell, sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol() - i))) {
                    needMerge = false;
                    break;
                }
            }
            if (needMerge) {
                final CellRangeAddress region = new CellRangeAddress(cellRef.getRow(), cellRef.getRow() + rows - 1,
                        cellRef.getCol() - cols + 1, cellRef.getCol());
                sheet.addMergedRegion(region);
                formatMergedRegion(region, sheet);
                return resultSize;
            } else {
                return resultSize;
            }
        } else {
            return resultSize;
        }
    }

    private Integer getIntValue(String expression, Context context) {
        ExpressionEvaluator expressionEvaluator = getTransformationConfig().getExpressionEvaluator();
        Object valueObj = expressionEvaluator.evaluate(expression, context.toMap());
        if (!(valueObj instanceof Integer)) {
            throw new JxlsException(expression + " expression is not a Integer");
        }
        Integer intVal = (Integer) valueObj;
        return intVal < 1 ? 0 : intVal;
    }

    private boolean compareValue(Cell before, Cell after) {

        try {
            if (before == null || after == null) {
                return false;
            }
            if (before.getCellTypeEnum() == after.getCellTypeEnum()) {
                switch (before.getCellTypeEnum()) {
                    case NUMERIC:
                        return before.getNumericCellValue() == after.getNumericCellValue() ? true : false;
                    case STRING:
                        return before.getStringCellValue().equals(after.getStringCellValue()) ? true : false;
                    case BOOLEAN:
                        return before.getBooleanCellValue() == after.getBooleanCellValue() ? true : false;
                    case FORMULA:
                        break;
                    default:
                        // 位置类型
                        return before.getStringCellValue().equals(after.getStringCellValue()) ? true : false;
                }
                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Command addArea(Area area) {
        super.addArea(area);
        this.area = area;
        return this;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setDynamicCols(String dynamicCols) {
        this.dynamicCols = dynamicCols;
    }

    public void setBorderConfigs(String borderConfigs) {
        this.borderConfigs = borderConfigs;
        if (StringUtils.isNotBlank(borderConfigs)) {
            final Map<String, String> configs = MAP_SPLITTER.split(borderConfigs);
            this.boarderConfigMaps = configs;
        }
    }

    private interface BorderTypeProcessor {
        void process(BorderStyle borderStyle, CellRangeAddress region, Sheet sheet);
    }
}
