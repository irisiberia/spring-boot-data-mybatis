package com.example.springbootdatamybatis.mapper;

import com.example.springbootdatamybatis.bean.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BillOrderMapper {

    @Select("select * from bill_order where id=#{id}")
    BillOrder getById(Long id);

    @Select("select count(id) from bill_order")
    int count();

    @Select("select * from bill_order order by id limit #{offset},#{limit}")
    List<BillOrder> queryByRequest(Req request);


    @Select("select * from bill_order  order by id  limit #{limit.offset}, #{limit.count} ")
    List<BillOrderVo> queryByLimit(LimitReq limitReq);


    @Select("SELECT a.order_id as orderId  ,a.id as id, b.bill_order_code,b.`name` from bill_order a LEFT JOIN bill_order_demo b on a.order_id=b.order_id " +
            "where a.order_id=#{orderId}")
    List<BillOrderVo> queryDemo(String orderId);

}
