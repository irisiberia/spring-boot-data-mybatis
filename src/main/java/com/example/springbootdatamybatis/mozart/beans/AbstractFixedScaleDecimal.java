package com.example.springbootdatamybatis.mozart.beans;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:41
 **/

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 抽象的{@link BigDecimal}，具备scale功能。
 *
 * @author Daniel Li
 * @since 16 May 2017
 */
public class AbstractFixedScaleDecimal<T extends AbstractFixedScaleDecimal> implements Comparable<T>, Serializable {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    private static final long serialVersionUID = -5440211037559482323L;

    protected final int scale;

    protected BigDecimal amount;

    protected transient Constructor<T> constructor;

    protected AbstractFixedScaleDecimal(int scale) {
        this(BigDecimal.valueOf(0), scale);
    }

    protected AbstractFixedScaleDecimal(BigDecimal amount, int scale) {
        this.scale = scale;
        this.amount = BigDecimal.valueOf(amount.movePointRight(scale).setScale(0, ROUNDING_MODE).longValue(), scale);
    }


    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isPositiveOrZero() {
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isNegativeOrZero() {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public T plus(T... values) {
        BigDecimal total = amount;
        for (T value : values) {
            total = total.add(value.amount, MATH_CONTEXT);
        }
        return with(total);
    }

    public T plus(T value) {
        if (value.amount.compareTo(BigDecimal.ZERO) == 0) {
            return asDerivedType();
        }
        return with(amount.add(value.amount, MATH_CONTEXT));
    }

    public T minus(T... values) {
        BigDecimal total = amount;
        for (T value : values) {
            total = total.subtract(value.amount, MATH_CONTEXT);
        }
        return with(total);
    }

    public T minus(T value) {
        if (value.amount.compareTo(BigDecimal.ZERO) == 0) {
            return asDerivedType();
        }
        return with(amount.subtract(value.amount, MATH_CONTEXT));
    }

    public T multiply(BigDecimal value) {
        if (value.compareTo(BigDecimal.ONE) == 0) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.multiply(value, MATH_CONTEXT);
        return with(newAmount);
    }

    public T multiply(double value) {
        if (value == 1) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(value), MATH_CONTEXT);
        return with(newAmount);
    }

    public T multiply(long value) {
        if (value == 1) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(value), MATH_CONTEXT);
        return with(newAmount);
    }

    public T divide(BigDecimal value) {
        if (value.compareTo(BigDecimal.ONE) == 0) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.divide(value, MATH_CONTEXT);
        return with(newAmount);
    }

    public T divide(double value) {
        if (value == 1) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.divide(BigDecimal.valueOf(value), MATH_CONTEXT);
        return with(newAmount);
    }

    public T divide(long value) {
        if (value == 1) {
            return asDerivedType();
        }
        BigDecimal newAmount = amount.divide(BigDecimal.valueOf(value), MATH_CONTEXT);

        return with(newAmount);
    }

    public T negate() {
        if (isZero()) {
            return asDerivedType();
        }
        return with(amount.negate());
    }

    private T with(BigDecimal newAmount) {
        if (newAmount.equals(amount)) {
            return asDerivedType();
        }
        return BeanUtils.instantiateClass(constructor, newAmount);
    }

    public T abs() {
        return (isNegative() ? negate() : asDerivedType());
    }

    public boolean greaterThan(T other) {
        return compareTo(other) > 0;
    }

    public boolean lessThan(T other) {
        return compareTo(other) < 0;
    }

    public long getAmount() {
        return amount.movePointRight(scale).setScale(0, ROUNDING_MODE).longValue();
    }

    public void setAmount(long amount) {
        this.amount = BigDecimal.valueOf(amount, scale);
    }

    public BigDecimal toBigDecimal() {
        return this.amount;
    }

    public BigDecimal toBigDecimal(int scale) {
        return BigDecimal.valueOf(amount.movePointRight(scale).setScale(0, ROUNDING_MODE).longValue(), scale);
    }

    public BigDecimal stripTrailingZeros() {
        return this.amount.stripTrailingZeros();
    }

    @Override
    public int compareTo(T o) {
        return amount.compareTo(o.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!this.getClass().isInstance(o)) return false;

        T value = (T) o;

        return amount != null ? amount.equals(value.amount) : value.amount == null;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    private T asDerivedType() {
        return (T) this;
    }

    {
        try {
            // 需要查询父类
            constructor = (Constructor<T>) this.getClass().getDeclaredConstructor(BigDecimal.class);
            constructor.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("必须包含带有BigDecimal类型的构造方法");
        }
    }
}