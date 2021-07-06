package com.example.springbootdatamybatis.common.aspect.annotationTest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: he.zhou
 * @Date: 2019-02-01
 * <p>
 * <p>
 * 基于注解@Aspect的AOP实现
 * com.automan.siberia.annotationTest下面所有的方法都能实现aop
 */
//@Aspect
//@Component
public class SysLogAspectOne {

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容
     *
     * @param jp
     */
    @Before("execution(* com.automan.siberia.aspect.annotationTest.*.*(..))")
    public void beforeMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println("【前置通知】the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
    }

    /**
     * 返回通知：目标方法正常执行完毕时执行以下代码
     *
     * @param jp
     * @param result
     */
    @AfterReturning(value = "execution(* com.automan.siberia.aspect.annotationTest.*.*(..))", returning = "result")
    public void afterReturningMethod(JoinPoint jp, Object result) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Method methodReal = null;
        try {
            methodReal = jp.getTarget().getClass().getDeclaredMethod(method.getName(),
                    method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //通过反射也能获取到自定义注解
        LogAnnotation tranSpondAspect = methodReal.getAnnotation(LogAnnotation.class);
        LogAnnotation tranSpondAspect1 = methodReal.getDeclaredAnnotation(LogAnnotation.class);
        if (Objects.isNull(tranSpondAspect)) {
            System.out.println("没有添加自定义注解");
        } else {
            System.out.println("添加了自定义注解" + tranSpondAspect.value());
        }

        String methodName = jp.getSignature().getName();

        System.out.println("【返回通知】the method 【" + methodName + "】 ends with 【" + result + "】");
    }

    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管是否发生异常。
     *
     * @param jp
     */
    @After("execution(* com.automan.siberia.aspect.annotationTest.*.*(..))")
    public void afterMethod(JoinPoint jp) {
        System.out.println("【后置通知】this is a afterMethod advice...");
    }

    /**
     * 异常通知：目标方法发生异常的时候执行以下代码
     */
    @AfterThrowing(value = "execution(* com.automan.siberia.aspect.annotationTest.*.*(..))", throwing = "e")
    public void afterThorwingMethod(JoinPoint jp, NullPointerException e) {
        String methodName = jp.getSignature().getName();
        System.out.println("【异常通知】the method 【" + methodName + "】 occurs exception: " + e);
    }

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     *
     * @return
     */
    @Around(value = "execution(* com.automan.siberia.aspect.annotationTest.*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        Object result = null;
        try {
            System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
            //执行目标方法
            result = jp.proceed();
            System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with " + result);
        } catch (Throwable e) {
            System.out.println("【环绕通知中的--->异常通知】：the method 【" + methodName + "】 occurs exception " + e);
        }

        System.out.println("【环绕通知中的--->后置通知】：-----------------end.----------------------");
        return 9999;
    }

}
