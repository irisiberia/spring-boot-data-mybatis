package com.example.springbootdatamybatis.mozart.iterator;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:37
 **/
public interface PagerIteratorWrapper<E> {
    /**
     * 为Iterator获取值的接口，返回null时，Iterator终止迭代
     *
     * @param pageNo 页号
     * @param pageSize 页大小
     * @return
     */
    E wrapper(int pageNo, int pageSize);
}
