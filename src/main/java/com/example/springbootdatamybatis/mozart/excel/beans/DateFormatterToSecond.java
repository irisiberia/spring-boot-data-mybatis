package com.example.springbootdatamybatis.mozart.excel.beans;

import com.example.springbootdatamybatis.mozart.excel.utils.DateFormatUtils;

import java.util.Date;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:08
 **/
public class DateFormatterToSecond extends DataFormatter {
    @Override
    protected boolean accept(Object value) {
        return value instanceof Date;
    }

    @Override
    protected Object apply(Object value) {
        return DateFormatUtils.format4y2M2d2h2m2s((Date) value);
    }
}
