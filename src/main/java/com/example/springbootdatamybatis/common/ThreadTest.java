package com.example.springbootdatamybatis.common;/** * @Author he.zhou * @Date 2020-07-11 */public class ThreadTest {    public static void main(String[] args) {        ThreadLocal<String> sThreadLocal = new ThreadLocal<String>();        sThreadLocal.set("23232323");        System.out.println(sThreadLocal.get());    }}