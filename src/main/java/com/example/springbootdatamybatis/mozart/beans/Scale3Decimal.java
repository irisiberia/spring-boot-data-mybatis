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
 * Created by chao.ma on 17/8/24.
 */
public class Scale3Decimal extends AbstractFixedScaleDecimal<Scale3Decimal> implements Serializable {
    private static final long serialVersionUID = -881049445079265626L;

    private static final int SCALE = 3;

    @Deprecated
    public Scale3Decimal() {
        super(SCALE);
    }

    /**
     * type handler 专用
     * @param amount
     */
    @Deprecated
    public Scale3Decimal(BigDecimal amount) {
        super(amount, SCALE);
    }

    public static Scale3Decimal create(BigDecimal decimal) {
        if (decimal == null) {
            return new Scale3Decimal(BigDecimal.ZERO);
        }
        BigDecimal scale4 = decimal.setScale(4, BigDecimal.ROUND_DOWN);
        return new Scale3Decimal(scale4);
    }
}
