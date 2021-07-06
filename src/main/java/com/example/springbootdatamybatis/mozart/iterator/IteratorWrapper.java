package com.example.springbootdatamybatis.mozart.iterator;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:37
 **/
public interface IteratorWrapper<E> {
    /**
     * 为Iterator获取值的接口，返回null时，Iterator终止迭代
     *
     * @param startIndex 起始下标(offset)
     * @param maxCount 每次获取量(limit)
     * @return
     */
    E wrapper(int startIndex, int maxCount);
}
