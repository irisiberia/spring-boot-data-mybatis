package com.example.springbootdatamybatis.mozart.excel.utils;

import org.apache.commons.lang3.StringUtils;
import org.jxls.util.CellRefUtil;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:31
 **/
public class ExcelCellUtil {

    /**
     * 列名到列号的转换
     * @param colName 列名
     * @return 列号
     */
    public static int colNameToColNo(String colName) {
        return CellRefUtil.convertColStringToIndex(colName) + 1;
    }

    /**
     * 列号到列名的转换
     *
     * @param colNo 列号
     * @return 列名
     */
    public static String colNoToColName(int colNo) {
        if (colNo < 1) {
            return StringUtils.EMPTY;
        }
        return CellRefUtil.convertNumToColString(colNo - 1);
    }

}