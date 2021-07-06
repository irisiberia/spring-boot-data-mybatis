package com.example.springbootdatamybatis.bean;

import com.example.springbootdatamybatis.mozart.excel.export.annotations.ExcelCellData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillOrder {
    @ExcelCellData(header = "id")
    private Long id;
    @ExcelCellData(header = "billOrder")
    private String orderId;
    @ExcelCellData(header = "billOrderType")
    private String billOrderType;
    @ExcelCellData(header = "createTime")
    private Date createTime;
    @ExcelCellData(header = "updateTime")
    private Date updateTime;
}
