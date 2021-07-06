package com.example.springbootdatamybatis.mozart.beans;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
public class BusinessLog implements Serializable {

    private static final long serialVersionUID = 8210517123239800257L;

    private Long id;

    private String functionType;

    private String operatorType;

    private String operator;

    private String businessId;

    private String content;

    private Date createTime;

    private Date updateTime;

    private String operatorMobile;

    @Builder
    public BusinessLog(Long id,String functionType,String operatorType,String operator,String businessId,String content,Date createTime,Date updateTime,String operatorMobile){
        this.id = id;
        this.functionType = functionType;
        this.operatorType = operatorType;
        this.operator = operator;
        this.businessId = businessId;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.operatorMobile = operatorMobile;
    }


}