package com.example.springbootdatamybatis.common.aspect.AspectTest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import java.util.List;

/**
 * Created by wei.gao on 2017/7/26.
 * <p>
 * 为门店人员注入shopCode
 */
//@Aspect
//@Component
public class DataAuthenticationAdvice {

    private List<Authenticator> authenticators;

    public List<Authenticator> getAuthenticators() {
        return authenticators;
    }

    public void setAuthenticators(List<Authenticator> authenticators) {
        this.authenticators = authenticators;
    }

//    @PostConstruct
//    public void init() {
//        this.authenticators= Lists.newArrayList();
//    }

    /**
     * 智能货柜的不走权限验证
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.automan.siberia.aspect.AspectTest.Calculator..*.*(..)) ")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Authenticator tor : authenticators) {
            if (!tor.doAuthentication(joinPoint)) {
                throw new Exception("您无此操作权限");
            }
        }
        return joinPoint.proceed();
    }

}
