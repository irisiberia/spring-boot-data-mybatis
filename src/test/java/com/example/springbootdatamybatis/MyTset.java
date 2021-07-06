package com.example.springbootdatamybatis;
import java.util.Date;

import com.example.springbootdatamybatis.bean.BillOrder;
import com.example.springbootdatamybatis.bean.BillOrderDemo;
import com.example.springbootdatamybatis.bean.BillOrderVo;
import com.example.springbootdatamybatis.bean.OrderEntity;
import com.example.springbootdatamybatis.mapper.BillOrderDemoMapper;
import com.example.springbootdatamybatis.mapper.BillOrderMapper;
import com.example.springbootdatamybatis.mapper.OrderMapper;
import net.minidev.json.JSONUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author he.zhou
 * @Date 2020/5/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTset {


    @Resource
    private BillOrderMapper billOrderMapper;

    @Resource
    private BillOrderDemoMapper billOrderDemoMapper;

    @Resource
    private OrderMapper orderMapper;


    @Test
    public void test2() {
        List<BillOrderVo> list = billOrderMapper.queryDemo("1234");
        System.out.println(list.size());
    }


    @Test
    public void testInsert() {
        BillOrderDemo demo = new BillOrderDemo();
        demo.setBillOrderCode("weweew");
        demo.setName("2323");
        billOrderDemoMapper.insert(demo);
    }

    @Test
    public void test11(){

        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setUserId(12341);
        orderEntity.setBusinessId(121212);
        orderEntity.setComment("3232");

        OrderEntity orderEntity1=new OrderEntity();
        orderEntity1.setUserId(2323);
        orderEntity1.setBusinessId(233);
        orderEntity1.setComment("32323");


        OrderEntity orderEntity2=new OrderEntity();
        orderEntity2.setUserId(20);
        orderEntity2.setBusinessId(233);
        orderEntity2.setComment("32323");




        orderMapper.insert(orderEntity);
        orderMapper.insert(orderEntity1);
        orderMapper.insert(orderEntity2);
    }
}
