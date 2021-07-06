package com.example.springbootdatamybatis.common.aspect.AspectTest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by wei.gao on 2017/7/28.
 */
@Component
public class MethodOrderAuthenticator implements Authenticator {

    /**
     * 进行鉴权
     *
     * @return
     */
    @Override
    public boolean doAuthentication(ProceedingJoinPoint proceedingJoinPoint) {

        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Method methodReal = null;
        try {
            methodReal = proceedingJoinPoint.getTarget().getClass().getDeclaredMethod(method.getName(),
                    method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DataAuth tranSpondAspect = methodReal.getAnnotation(DataAuth.class);
        if (Objects.isNull(tranSpondAspect)) {
            return true;
        }
        AuthDescription[] authDescriptions = tranSpondAspect.authentications();
        //如果需要鉴权,必须有要鉴权的对象
        if (authDescriptions.length < 1) {
            try {
                throw new NoPermissionException("authDescriptions not be empty!!!");
            } catch (NoPermissionException e) {
                e.printStackTrace();
            }
        }
        //鉴权
        doAuthenticationInternal(authDescriptions, proceedingJoinPoint.getArgs());
        return true;
    }

    private void doAuthenticationInternal(AuthDescription[] authDescriptions, Object[] args) {
        {
            for (AuthDescription authDescription : authDescriptions) {
                boolean isSelf = authDescription.isSelf();
                int argumentIndex = authDescription.argumentIndex();
                String path = authDescription.path();
                if (path.equals("sss")) {
                    try {
                        throw new Throwable("鉴权失败");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                if (argumentIndex == 2) {
                    try {
                        throw new Throwable("鉴权失败");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }

                return;
            }
        }
    }


}
