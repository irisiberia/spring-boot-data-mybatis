package com.example.springbootdatamybatis.mozart.utils;

import java.math.BigDecimal;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:39
 **/
public class BigDecimalUtils {


    public static boolean equalsWith4Scale(BigDecimal a, BigDecimal b) {
        return create4scale(a.doubleValue()).equals(create4scale(b.doubleValue()));
    }

    public static BigDecimal create4scale(double value) {
        return new BigDecimal(value).setScale(4,BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal create4scale(String value) {
        return new BigDecimal(value).setScale(4,BigDecimal.ROUND_HALF_DOWN);
    }

}
