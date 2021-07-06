package com.example.springbootdatamybatis.common.aspect.annotationTest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author: he.zhou
 * @Date: 2018-12-04
 * <p>
 * 基于自定义注解实现的aop
 * @Pointcut("@annotation(com.automan.siberia.annotationTest.LogAnnotation)") 切入点为@interface注解，只要方法上有这个注解，就能实现代理
 */
//@Aspect
//@Component
public class SysLogAspect {

    @Pointcut("@annotation(com.automan.siberia.aspect.annotationTest.LogAnnotation)")
    public void tranSpond() {
    }

    @Before("tranSpond()")
    public void before(JoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Method methodReal = point.getTarget().getClass().getDeclaredMethod(method.getName(),
                method.getParameterTypes());
        LogAnnotation tranSpondAspect = method.getAnnotation(LogAnnotation.class);
        final String desc = tranSpondAspect.value();
        System.out.println(method + "======" + desc);
    }


    @Around("tranSpond()")
    public Object aroundMethod(ProceedingJoinPoint jp) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        String methodName = jp.getSignature().getName();
        Object result = null;
        try {
//            System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
            //执行目标方法
            result = jp.proceed();

            System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with " + result);
        } catch (Throwable e) {
//            System.out.println("【环绕通知中的--->异常通知】：the method 【" + methodName + "】 occurs exception " + e);
        }

//        System.out.println("【环绕通知中的--->后置通知】：-----------------end.----------------------");
        return result;
    }

    public static void main(String[] args) {
        for (Method m : ArithmeticCalculatorImpl.class.getDeclaredMethods()) {
            LogAnnotation lg = m.getAnnotation(LogAnnotation.class);
            Optional.ofNullable(lg).ifPresent(ar -> System.out.println(ar.value() + ar.name()));
        }
    }
}

