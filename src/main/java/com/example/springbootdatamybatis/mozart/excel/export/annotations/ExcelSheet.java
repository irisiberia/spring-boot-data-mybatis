package com.example.springbootdatamybatis.mozart.excel.export.annotations;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:17
 **/
/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 流式导出指定sheet名称
 *
 * @author fan.tang created on 2017/8/25 下午5:21.
 * @version 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
    String sheetName() default StringUtils.EMPTY;
}
