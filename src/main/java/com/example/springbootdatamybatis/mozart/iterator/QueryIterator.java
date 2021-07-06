package com.example.springbootdatamybatis.mozart.iterator;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:38
 **/
/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import java.util.Iterator;

/**
 *
 * 通用查询迭代器实现类，用于替代接口的循环调用
 *
 * <br/>
 * E 最终next的返回值类型
 *
 * <br/>
 * 调用方实现wrapper即可
 *
 * @author fan.tang created on 2017/11/14 上午11:01.
 * @version 1.0
 */
public class QueryIterator<E> implements Iterator<E> {

    protected final IteratorWrapper<E> wrapper;

    /**
     * 标识是否还有下一页
     */
    protected boolean hasNext = true;

    /**
     * 设置每次查询的量，默认为1
     */
    protected int maxCount = 1;

    protected E next;

    /**
     * 设置其实下标
     */
    protected int startIndex = 0;

    public QueryIterator(IteratorWrapper<E> wrapper) {
        this.wrapper = wrapper;
    }

    public QueryIterator(IteratorWrapper<E> wrapper, int maxCount) {
        this(wrapper);
        this.maxCount = maxCount;
    }

    @Override
    public boolean hasNext() {
        E e = wrapper.wrapper(startIndex, maxCount);
        if (e != null) {
            this.next = e;
            this.startIndex += maxCount;
            this.hasNext = true;
        } else {
            this.next = null;
            this.hasNext = false;
        }
        return hasNext;
    }

    @Override
    public E next() {
        return next;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
