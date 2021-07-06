package com.example.springbootdatamybatis.common.aspect.annotationTest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author he.zhou
 * @date 2019/9/26
 **/

//@Aspect
//@Component
public class SysLogAspectSecond {

    @Around("@annotation(logAnnotation)")
    public Object aroundMethod(ProceedingJoinPoint jp, LogAnnotation logAnnotation) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        String methodName = jp.getSignature().getName();
        Object result = null;
        return 0;
    }
}
