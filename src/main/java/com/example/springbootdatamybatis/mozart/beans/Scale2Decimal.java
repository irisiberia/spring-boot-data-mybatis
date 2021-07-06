package com.example.springbootdatamybatis.mozart.beans;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:23
 **/

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 固定精度为2位的Decimal实体
 * 使用create方法保证rounding方法生效
 * Created by fan.tang on 2017/7/11.
 */
public class Scale2Decimal  extends AbstractFixedScaleDecimal<Scale2Decimal> implements Serializable {
    private static final long serialVersionUID = -881049445079265626L;

    private static final int SCALE = 2;

    @Deprecated
    public Scale2Decimal() {
        super(SCALE);
    }

    /**
     * type handler 专用
     * @param amount
     */
    @Deprecated
    public Scale2Decimal(BigDecimal amount) {
        super(amount, SCALE);
    }

    public static Scale2Decimal create(BigDecimal decimal) {
        if (decimal == null) {
            return new Scale2Decimal(BigDecimal.ZERO);
        }
        BigDecimal scale3 = decimal.setScale(3, BigDecimal.ROUND_DOWN);
        return new Scale2Decimal(scale3);
    }
}
