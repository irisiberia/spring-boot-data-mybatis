package com.example.springbootdatamybatis.common.aspect.annotationTest;

import java.lang.annotation.*;

/**
 * @Author: he.zhou
 * @Date: 2018-12-04
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogAnnotation {
    String value() default "";

    String name() default "";
}
