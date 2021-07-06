package com.example.springbootdatamybatis.controller;

import com.example.springbootdatamybatis.Biz.BillOrderBiz;
import com.example.springbootdatamybatis.bean.BillOrder;
import com.example.springbootdatamybatis.bean.BillOrderDemo;
import com.example.springbootdatamybatis.bean.Req;
import com.example.springbootdatamybatis.mapper.BillOrderMapper;
import com.example.springbootdatamybatis.mozart.AbstactExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
public class BillOrderController {

    @Resource
    private BillOrderMapper billOrderMapper;

    @Resource
    private BillOrderBiz billOrderBiz;

    @Resource
    private AbstactExport abstactExport;


    @GetMapping("/billdedo")
    public BillOrderDemo getDemo(Long id) {
        return  billOrderBiz.getOne(id);
    }

    @GetMapping("/bill")
    private BillOrder getBillOrder() {
        return billOrderBiz.queryAll();
    }

    @GetMapping(value = "/bill/export")
    public void balancePaymentV2(HttpServletResponse response,
                                 HttpServletRequest request) {


        String downloadFileName = "促销补差明细.xlsx";
        Req req2 = Req.builder()
                .currentPage(1)
                .pageSize(10).build();

        try {
            Iterable<List<BillOrder>> iterable = billOrderBiz.test3(req2);
            abstactExport.download(downloadFileName, iterable, BillOrder.class, response);
        } catch (Exception e) {
            log.error("对账报表-促销补差明细列表导出失败", e);
        }

    }


}
