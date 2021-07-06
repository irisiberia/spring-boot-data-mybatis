package com.example.springbootdatamybatis.mozart.excel.beans;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:12
 **/
public class LongNumAsStringFormatter  extends DataFormatter {

    @Override
    public boolean accept(Object value) {
        return (value instanceof Long) || (value instanceof Integer);
    }

    @Override
    public Object apply(Object value) {
        return String.valueOf(value);
    }
}
