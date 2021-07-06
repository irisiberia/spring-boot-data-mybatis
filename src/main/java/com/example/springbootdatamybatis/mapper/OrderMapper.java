package com.example.springbootdatamybatis.mapper;

import com.example.springbootdatamybatis.bean.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xingerkang on 19/1/5.
 */
@Mapper
public interface OrderMapper {

    @Insert(" insert into order_entity#{tableId}(user_id,business_id,comment)" +
             " values(#{userId},#{businessId},#{comment})")
    long insert(OrderEntity entity);


    @Select("select * from ns_norder_d1_t#{tableId} where businessid = #{businessid} limit 1")
    OrderEntity getUnpayOrderByBusinessid(OrderEntity entity);

    @Select("select * from ns_norder_d1_t#{tableId} where businessid = #{businessid}")
    List<OrderEntity> getOrderByBusinessid(OrderEntity entity);

    @Update("update ns_norder_d1_t#{tableId} set status = 8,updatetime=#{updatetime} where businessid = #{businessid} and status=0")
    long finishOrder(OrderEntity entity);

    @Update("update ns_norder_d1_t#{tableId} set ruleresult = #{ruleresult},updatetime=#{updatetime} where businessid = #{businessid}")
    long updateRuleResult(OrderEntity entity);
}
