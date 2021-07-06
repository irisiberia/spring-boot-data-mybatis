package com.example.springbootdatamybatis.mozart.excel.export.command;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:19
 **/
/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import com.example.springbootdatamybatis.mozart.utils.Safes;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.CellRefUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 隐藏列命令
 *
 * @author fan.tang created on 2017/8/24 下午10:28.
 * @version 1.0
 */
public class HideColumnsCommand extends AbstractCommand {

    Area area;
    /**
     * 要隐藏的列
     */
    String colNames;

    private static Splitter SPLITTER_ON_COMMA = Splitter.on(',').trimResults().omitEmptyStrings();

    @Override
    public String getName() {
        return "hideColumns";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Size resultSize = area.applyAt(cellRef, context);
        if (resultSize.equals(Size.ZERO_SIZE)) {
            return resultSize;
        }

        if (StringUtils.isNotBlank(colNames)) {
            PoiTransformer transformer = (PoiTransformer) area.getTransformer();
            final Workbook workbook = transformer.getWorkbook();
            Sheet sheet = workbook.getSheet(cellRef.getSheetName());
            final Set<Integer> colIndexes = Safes.of(SPLITTER_ON_COMMA.splitToList(colNames)).stream()
                    .map(CellRefUtil::convertColStringToIndex).collect(Collectors.toSet());
            for (Integer colIndex : colIndexes) {
                sheet.setColumnHidden(colIndex, true);
            }
        }

        return resultSize;
    }

    @Override
    public Command addArea(Area area) {
        super.addArea(area);
        this.area = area;
        return this;
    }

    public void setColNames(String colNames) {
        this.colNames = colNames;
    }
}
