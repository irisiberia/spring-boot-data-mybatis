package com.example.springbootdatamybatis.mozart.excel.export.command;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:21
 **/

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.CellRefUtil;


/**
 * 支持动态打印区域
 *
 * Created by yunan on 2017/6/26.
 */
public class PrintTitleCommand extends AbstractCommand {

    Area area;
    /**
     * 开始列 从0开始
     */
    String startCol;
    /**
     * 列数
     */
    String colNum;
    /**
     * 开始行 从1开始
     */
    String startRow;
    /**
     * 行数
     */
    String rowNum;

    @Override
    public String getName() {
        return "dynamicPrintTitle";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Size resultSize = area.applyAt(cellRef, context);
        if (resultSize.equals(Size.ZERO_SIZE))
            return resultSize;
        PoiTransformer transformer = (PoiTransformer) area.getTransformer();
        Workbook workbook = transformer.getWorkbook();
        int startColumn = NumberUtils.toInt(startCol);
        int endColumn = startColumn + NumberUtils.toInt(colNum);
        int startR = NumberUtils.toInt(startRow);
        int endR = startR + NumberUtils.toInt(rowNum);
        Sheet sheet = workbook.getSheet(cellRef.getSheetName());
        sheet.setRepeatingRows(CellRangeAddress.valueOf(startR+":"+endR));
        sheet.setRepeatingColumns(CellRangeAddress.valueOf(CellRefUtil.convertNumToColString(startColumn)+":"+CellRefUtil.convertNumToColString(endColumn)));
        return resultSize;
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

    public String getColNum() {
        return colNum;
    }

    public void setColNum(String colNum) {
        this.colNum = colNum;
    }

    public String getStartRow() {
        return startRow;
    }

    public void setStartRow(String startRow) {
        this.startRow = startRow;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }
}
