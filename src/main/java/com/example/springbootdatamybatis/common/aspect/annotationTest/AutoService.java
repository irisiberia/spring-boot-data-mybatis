package com.example.springbootdatamybatis.common.aspect.annotationTest;

import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: he.zhou
 * @Date: 2018-12-04
 */
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AutoService {
    @Resource
    private AutoService2 autoService2;

    @LogAnnotation(value = "我是计算测试", name = "qwqwq")
    public int add(int i, int j) {

        int result = i + j;

//        int re = autoService2.substract(i, j);
        AutoService autoService= (AutoService) AopContext.currentProxy();
        int re1 =  autoService.substract2(4,5);
        System.out.println("调用减法");
        return result;
    }


    @LogAnnotation(value = "我是计算测试减法", name = "323233")
    public int substract2(int i, int j) {

        int result = i + j;
        return result;
    }

    
}
