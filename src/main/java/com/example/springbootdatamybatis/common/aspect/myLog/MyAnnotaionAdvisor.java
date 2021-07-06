package com.example.springbootdatamybatis.common.aspect.myLog;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

public class MyAnnotaionAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;
    private Pointcut pointcut;

    //传入你自定义注解和MethodInterceptor实现
    public MyAnnotaionAdvisor (Class<? extends Annotation> annotationType, MethodInterceptor interceptor){
        this.advice = (Advice) interceptor;
        this.pointcut = buildPointcut(annotationType);
    }

    @Override
    public Pointcut getPointcut() {
        Assert.notNull(this.pointcut, "'pointcut' must not be null");
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        Assert.notNull(this.advice, "'advice' must not be null");
        return this.advice;
    }

    private Pointcut buildPointcut(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "'annotationTypes' must not be null");
        ComposablePointcut result = null;
        //类级别
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType, true);
        //方法级别
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(annotationType);
        //对于类和方法上都可以添加注解的情况
        //类上的注解,最终会将注解绑定到每个方法上
        if (result == null) {
            result = new ComposablePointcut(cpc);
        }
        return result.union(mpc);
    }
}
