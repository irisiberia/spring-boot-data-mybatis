package com.example.springbootdatamybatis.mozart.iterator;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:37
 * <p>
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 * <p>
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 * <p>
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */
/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import com.example.springbootdatamybatis.mozart.beans.Limit;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 分页列表迭代器
 *
 * @author fan.tang created on 2017/12/18 下午6:14.
 * @version 1.0
 */
public class PagerListQueryIterator<E extends List> extends QueryIterator<E> {

    private int pageSize = 15;

    private int pageNo = 1;

    public PagerListQueryIterator(PagerIteratorWrapper<E> pagerIterWrapper) {
        super((startIndex, maxCount) -> {
            final Limit limit = Limit.createByOffset(startIndex, maxCount);
            return pagerIterWrapper.wrapper(limit.getPage(), limit.getSize());
        });

        this.maxCount = pageSize;
        this.startIndex = 0;
    }

    public PagerListQueryIterator(PagerIteratorWrapper<E> pagerIterWrapper, int pageSize, int pageNo) {
        super((startIndex, maxCount) -> {
            final Limit limit = Limit.createByOffset(startIndex, maxCount);
            return pagerIterWrapper.wrapper(limit.getPage(), limit.getSize());
        });
        final Limit limit = Limit.createByPage(pageNo, pageSize);
        this.maxCount = limit.getSize();
        this.startIndex = limit.getOffset();
    }

    @Override
    public boolean hasNext() {
        E e = wrapper.wrapper(startIndex, maxCount);
        if (CollectionUtils.isNotEmpty(e)) {
            this.next = e;
            this.startIndex += maxCount;
            this.hasNext = true;
        } else {
            this.next = null;
            this.hasNext = false;
        }
        return this.hasNext;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
