package com.example.springbootdatamybatis.mapper;

import com.example.springbootdatamybatis.bean.BillOrderDemo;
import com.example.springbootdatamybatis.bean.BillOrderDemoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface BillOrderDemoMapper {
    int countByExample(BillOrderDemoExample example);

    int deleteByExample(BillOrderDemoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BillOrderDemo record);

    int insertSelective(BillOrderDemo record);

    List<BillOrderDemo> selectByExample(BillOrderDemoExample example);

    BillOrderDemo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BillOrderDemo record, @Param("example") BillOrderDemoExample example);

    int updateByExample(@Param("record") BillOrderDemo record, @Param("example") BillOrderDemoExample example);

    int updateByPrimaryKeySelective(BillOrderDemo record);

    int updateByPrimaryKey(BillOrderDemo record);
}