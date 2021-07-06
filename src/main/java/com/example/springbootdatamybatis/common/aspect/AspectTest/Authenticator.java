package com.example.springbootdatamybatis.common.aspect.AspectTest;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by wei.gao on 2017/7/28.
 */
public interface Authenticator {

    /**
     * 进行鉴权
     * <p>
     * 这个方法可以直接抛出异常
     *
     * @return true 表示鉴权正常,可继续执行 false 将在上一层抛出
     */
    boolean doAuthentication(ProceedingJoinPoint proceedingJoinPoint);
}
