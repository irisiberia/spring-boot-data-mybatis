package com.example.springbootdatamybatis.mozart.dynamic.datasource;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:01
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface DBType {

    /**
     * 指定使用的db
     */
    String value() default "";
}

