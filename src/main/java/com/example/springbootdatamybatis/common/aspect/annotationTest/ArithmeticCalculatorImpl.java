package com.example.springbootdatamybatis.common.aspect.annotationTest;

import org.springframework.stereotype.Component;

/**
 * @Author: he.zhou
 * @Date: 2019-02-01
 */
@Component
public class ArithmeticCalculatorImpl implements ArithmeticCalculator {
    @Override
    @LogAnnotation(value = "bjdbhjbjsbjhsbchjsb",name = "2019-06-23测试")
    public int add(int i, int j) {
        int result = i + j;
//        ArithmeticCalculator arithmeticCalculator= (ArithmeticCalculator) AopContext.currentProxy();
//        int result2=arithmeticCalculator.sub(i,j);
        return result;
    }

    @Override
    @LogAnnotation(value = "hvhgdhkfhdkd")
    public int sub(int i, int j) {
        int result = i - j;
        return result;
    }

    @Override
    public int mul(int i, int j) {
        int result = i * j;
        return result;
    }

    @Override
    public int div(int i, int j) {
        int result = i / j;
        return result;
    }
}
