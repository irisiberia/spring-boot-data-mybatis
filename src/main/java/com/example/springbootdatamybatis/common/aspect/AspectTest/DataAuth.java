package com.example.springbootdatamybatis.common.aspect.AspectTest;

import java.lang.annotation.*;

/**
 * Created by wei.gao on 2017/7/28.
 *
 * 对订单进行数据权限验证
 */
@Target({ElementType.PARAMETER,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataAuth {

    /**
     * 要验证的权限
     * @return
     */
    AuthDescription[] authentications();
}
