package com.example.springbootdatamybatis.bean;

import com.example.springbootdatamybatis.mozart.excel.export.annotations.ExcelCellData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author he.zhou
 * @Date 2020/5/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillOrderVo {

    private Long id;
    private String orderId;
    private String billOrderType;
    private String bill_order_code;
    private String name;

}
