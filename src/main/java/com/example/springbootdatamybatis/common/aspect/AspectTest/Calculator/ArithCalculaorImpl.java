package com.example.springbootdatamybatis.common.aspect.AspectTest.Calculator;


import com.automan.siberia.aspect.AspectTest.AuthDescription;
import com.automan.siberia.aspect.AspectTest.DataAuth;
import org.springframework.stereotype.Component;

/**
 * @Author: he.zhou
 * @Date: 2019-02-09
 */
@Component
public class ArithCalculaorImpl implements ArithCalculaor {
    @Override
    @DataAuth(authentications = {@AuthDescription(path = "sss")})
    public int add(int i, int j) {
        int result = i + j;
        return result;
    }

    @Override
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
