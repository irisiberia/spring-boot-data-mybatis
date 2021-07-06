package com.example.springbootdatamybatis.mozart.excel.beans;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:11
 **/
public class DefaultDataFormatter extends DataFormatter {
    @Override
    public boolean accept(Object value) {
        return false;
    }

    @Override
    public Object apply(Object value) {
        // 默认实现，什么都不做直接原样返回
        return value;
    }
}