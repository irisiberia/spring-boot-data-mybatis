package com.example.springbootdatamybatis.mozart.dynamic.datasource;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:02
 **/

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 动态数据库切换切面 Created by chao.ma on 16/12/16.
 */
@Aspect
public class DynamicDBAop {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.example.springbootdatamybatis.mozart.dynamic.datasource.DBType)")
    public void serviceExecution() {
    }

    @Around("serviceExecution()")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {

        DBType type = getMethodAnnotation(pjp, DBType.class);
        String origType = DbContextHolder.getDbType();
        try {

            DbContextHolder.setDbType(type.value());
            return pjp.proceed();
        } catch (Throwable throwable) {
            log.warn("error while processing method", throwable);
            throw throwable;
        } finally {
            if (origType != null) {
                DbContextHolder.setDbType(origType);
            } else {
                DbContextHolder.clearDbType();
            }
        }
    }

    /**
     * Get value of annotated method parameter
     */
    private <T extends Annotation> T getMethodAnnotation(JoinPoint joinPoint, Class<T> clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(clazz);
    }
}
