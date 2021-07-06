package com.example.springbootdatamybatis.common.aspect.AspectTest;

import java.lang.annotation.*;

/**
 * Created by wei.gao on 2017/7/28.
 *
 * 这个注解可以描述参数的位置
 * if this annotation in METHOD
 * 根据 参数索引和路径可以确定要取值的路径
 * else if PARAMETER
 * 如果在参数上根据 isSelf 和 path
 * else FIELD
 * 根据isSelf和Path
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthDescription {

    /**
     * 是否是它本身
     * @return
     */
    boolean isSelf() default false;

    /**
     * 需要验证的参数的路径
     * @return
     */
    String path() default "";

    /**
     *
     * @return
     */
    int argumentIndex() default 0;

    /**
     * 订单号是否必须存在
     * @return
     */
    boolean required() default true;

    /**
     * 验证的类型,目前只有订单
     * @return
     */
    int type() default 0;
}
