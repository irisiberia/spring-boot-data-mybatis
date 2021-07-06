package com.example.springbootdatamybatis.common.aspect.myLog;

import com.automan.siberia.observation.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class MyInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //获取目标类
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        //获取指定方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        //获取真正执行的方法,可能存在桥接方法
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        //获取方法上注解
        Async async = AnnotatedElementUtils.findMergedAnnotation(userDeclaredMethod, Async.class);
        if (async == null) {
            async = AnnotatedElementUtils.findMergedAnnotation(userDeclaredMethod.getDeclaringClass(), Async.class);
        }

        //获取返回类型
        Class<?> returnType = invocation.getMethod().getReturnType();
        //返回类型判断
        if (User.class.isAssignableFrom(returnType)) {

            return null;
        }

        //执行具体业务逻辑

        return invocation.proceed();
    }
}
