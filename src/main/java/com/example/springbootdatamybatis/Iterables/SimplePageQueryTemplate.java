package com.example.springbootdatamybatis.Iterables;

import java.util.List;

/**
 * @Author: he.zhou
 * @Date: 2019-05-17
 */
public interface SimplePageQueryTemplate<T> {
    /**
     * 类似SQL的start, limit，访问从start开始的limit记录，返回小于limit条记录表示没有更多数据可以访问
     */
    List<T> query(int pageNo, int pageSize);
}
