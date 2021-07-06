package com.example.springbootdatamybatis.mozart.excel.export.command;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:21
 **/

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.JxlsException;
import org.jxls.common.Size;
import org.jxls.expression.ExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;

/**
 * 支持动态打印区域
 *
 * Created by yunan on 2017/6/26.
 */
public class PrintAreaCommand extends AbstractCommand {

    Area area;
    /**
     * 开始列 从0开始
     */
    String startCol;

    /**
     *
     */
    String colSize;

    String rowSize;

    String extraColSize;

    String extraRowSize;

    @Override
    public String getName() {
        return "dynamicPrintArea";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Size resultSize = area.applyAt(cellRef, context);
        if (resultSize.equals(Size.ZERO_SIZE))
            return resultSize;
        PoiTransformer transformer = (PoiTransformer) area.getTransformer();
        Workbook workbook = transformer.getWorkbook();
        int startColumn = NumberUtils.toInt(startCol);
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int sheetIndex=0;sheetIndex<numberOfSheets;sheetIndex++) {
            if (workbook.getSheetAt(sheetIndex).getSheetName().equals(cellRef.getSheetName())){
                workbook.setPrintArea(sheetIndex, startColumn, getSizeValue(colSize,context)+NumberUtils.toInt(extraColSize), 0,getSizeValue(rowSize,context)+NumberUtils.toInt(extraRowSize));
            }

        }
        return resultSize;
    }

    private Integer getSizeValue(String expression, Context context) {
        ExpressionEvaluator expressionEvaluator = getTransformationConfig().getExpressionEvaluator();
        Object valueObj = expressionEvaluator.evaluate(expression, context.toMap());
        if (!(valueObj instanceof Integer)) {
            throw new JxlsException("dynamicPrintArea... " +expression + " expression is not a Integer");
        }
        Integer intVal = (Integer) valueObj;
        return intVal < 1 ? 0 : intVal;
    }


    @Override
    public Command addArea(Area area) {
        super.addArea(area);
        this.area = area;
        return this;
    }

    public String getStartCol() {
        return startCol;
    }

    public void setStartCol(String startCol) {
        this.startCol = startCol;
    }

    public String getRowSize() {
        return rowSize;
    }

    public void setRowSize(String rowSize) {
        this.rowSize = rowSize;
    }

    public String getColSize() {
        return colSize;
    }

    public void setColSize(String colSize) {
        this.colSize = colSize;
    }

    public String getExtraColSize() {
        return extraColSize;
    }

    public void setExtraColSize(String extraColSize) {
        this.extraColSize = extraColSize;
    }

    public String getExtraRowSize() {
        return extraRowSize;
    }

    public void setExtraRowSize(String extraRowSize) {
        this.extraRowSize = extraRowSize;
    }
}
