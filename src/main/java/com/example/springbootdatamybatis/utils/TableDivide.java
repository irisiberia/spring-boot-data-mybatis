package com.example.springbootdatamybatis.utils;

import java.util.HashMap;

/**
 * @Author he.zhou
 * @Date 2020/5/30
 */
public abstract class TableDivide {
    private String tableId;


    public static final int ORDER_TABLE_NUMBER = 3;


    public int getTableId() {

        if (getDividedId() == 0 || getTableNumber() == 0) {
            throw new RuntimeException("表格式异常");
        }

        return 1000 + (int) (getDividedId() % getTableNumber());
    }

    protected abstract int getTableNumber();

    protected abstract long getDividedId();

}
