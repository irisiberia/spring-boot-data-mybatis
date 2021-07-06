package com.example.springbootdatamybatis.common.aspect.BeanPostProcessorTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * @Author: he.zhou
 * @Date: 2019-01-31
 * <p>
 * BeanPostProcessor【interface】:bean后置处理器
 * 在bean初始化前后进行一些工作处理
 * postProcessBeforeInitialization
 * postProcessAfterInitialization
 */
//@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
            System.out.println("postProcessBeforeInitialization------" + s + "===>" + o);
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (s.equals("alertRecordService")) {
            System.out.println("postProcessAfterInitialization------" + s + "===>" + o);
        }

        return o;
    }
}
