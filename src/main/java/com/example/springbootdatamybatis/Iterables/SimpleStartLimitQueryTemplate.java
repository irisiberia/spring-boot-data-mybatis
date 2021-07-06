package com.example.springbootdatamybatis.Iterables;

import java.util.List;

/**
 * mysql的limit方式查询
 */
public interface
SimpleStartLimitQueryTemplate<T> {
    /**
     * 类似SQL的start, limit，访问从start开始的limit记录，返回小于limit条记录表示没有更多数据可以访问
     */
    List<T> query(int start, int limit);
}
