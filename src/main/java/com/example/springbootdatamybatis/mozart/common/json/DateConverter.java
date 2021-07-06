package com.example.springbootdatamybatis.mozart.common.json;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:39
 **/
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        return JsonUtil.of(source, Date.class);
    }

    public static void main(String[] args) {
        DateConverter dateConverter = new DateConverter();
        String date = JsonUtil.toJson(new Date());
        System.out.println(dateConverter.convert(date));
    }
}