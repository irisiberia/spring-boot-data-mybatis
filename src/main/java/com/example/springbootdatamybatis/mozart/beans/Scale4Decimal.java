package com.example.springbootdatamybatis.mozart.beans;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:24
 **/

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 固定精度为4位的Decimal实体
 * 使用create方法保证rounding方法生效
 * Created by fan.tang on 2017/7/11.
 */
public class Scale4Decimal  extends AbstractFixedScaleDecimal<Scale4Decimal> implements Serializable {
    private static final long serialVersionUID = -6496852068166843022L;

    public static final int SCALE = 4;

    @Deprecated
    public Scale4Decimal() {
        super(SCALE);
    }

    /**
     * type handler 专用
     * @param amount
     */
    @Deprecated
    public Scale4Decimal(BigDecimal amount) {
        super(amount, SCALE);
    }

    public static Scale4Decimal create(BigDecimal decimal) {
        if (decimal == null) {
            return new Scale4Decimal(BigDecimal.ZERO);
        }
        BigDecimal scale5 = decimal.setScale(5,BigDecimal.ROUND_DOWN);
        return new Scale4Decimal(scale5);
    }
}
