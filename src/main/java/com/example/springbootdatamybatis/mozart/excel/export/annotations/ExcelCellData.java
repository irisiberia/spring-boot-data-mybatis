package com.example.springbootdatamybatis.mozart.excel.export.annotations;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:16
 **/

import com.example.springbootdatamybatis.mozart.excel.beans.DataFormatter;
import com.example.springbootdatamybatis.mozart.excel.beans.DefaultDataFormatter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fan.tang on 2017/6/15.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCellData {

    /**
     * 导出头字段
     *
     * @return
     */
    String header();

    /**
     * 数据格式化器，用于对单元格数据在写入excel前的预处理
     *
     * @return
     */
    Class<? extends DataFormatter> dataFormatter() default DefaultDataFormatter.class;

    /**
     * 单元格格式, 设置单元格的格式, 常见格式如下
     *
     * <p/>
     * 0, "General"<br/>
     * 1, "0"<br/>
     * 2, "0.00"<br/>
     * 3, "#,##0"<br/>
     * 4, "#,##0.00"<br/>
     * 5, "$#,##0_);($#,##0)"<br/>
     * 6, "$#,##0_);[Red]($#,##0)"<br/>
     * 7, "$#,##0.00);($#,##0.00)"<br/>
     * 8, "$#,##0.00_);[Red]($#,##0.00)"<br/>
     * 9, "0%"<br/>
     * 0xa, "0.00%"<br/>
     * 0xb, "0.00E+00"<br/>
     * 0xc, "# ?/?"<br/>
     * 0xd, "# ??/??"<br/>
     * 0xe, "m/d/yy"<br/>
     * 0xf, "d-mmm-yy"<br/>
     * 0x10, "d-mmm"<br/>
     * 0x11, "mmm-yy"<br/>
     * 0x12, "h:mm AM/PM"<br/>
     * 0x13, "h:mm:ss AM/PM"<br/>
     * 0x14, "h:mm"<br/>
     * 0x15, "h:mm:ss"<br/>
     * 0x16, "m/d/yy h:mm"<br/>
     * <p/>
     * // 0x17 - 0x24 reserved for international and undocumented 0x25, "#,##0_);(#,##0)"<br/>
     * 0x26, "#,##0_);[Red](#,##0)"<br/>
     * 0x27, "#,##0.00_);(#,##0.00)"<br/>
     * 0x28, "#,##0.00_);[Red](#,##0.00)"<br/>
     * 0x29, "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)"<br/>
     * 0x2a, "_($* #,##0_);_($* (#,##0);_($* \"-\"_);_(@_)"<br/>
     * 0x2b, "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"<br/>
     * 0x2c, "_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)"<br/>
     * 0x2d, "mm:ss"<br/>
     * 0x2e, "[h]:mm:ss"<br/>
     * 0x2f, "mm:ss.0"<br/>
     * 0x30, "##0.0E+0"<br/>
     * 0x31, "@" - This is text format.<br/>
     * 0x31 "text" - Alias for "@"<br/>
     * <p/>
     */
    String cellStyle() default StringUtils.EMPTY;
}
