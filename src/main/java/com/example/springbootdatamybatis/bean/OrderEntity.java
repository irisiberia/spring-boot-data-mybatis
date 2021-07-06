package com.example.springbootdatamybatis.bean;

import com.example.springbootdatamybatis.utils.TableDivide;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xingerkang on 19/1/5.
 */
@Data
@ToString
public class OrderEntity extends TableDivide {
    private long id;
    private long userId;
    private long businessId;
    private String comment;
    private Date createTime;
    private Date updateTime;


    @Override
    public int getTableNumber() {
        return ORDER_TABLE_NUMBER;
    }

    @Override
    public long getDividedId() {
        return userId;
    }
}
