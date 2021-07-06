package com.example.springbootdatamybatis.mozart.dynamic.datasource;

import org.springframework.stereotype.Component;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:00
 **/
@Component
public class DbContextHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal();

    @SuppressWarnings("unchecked")
    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return (String) contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
