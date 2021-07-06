package com.example.springbootdatamybatis.mozart.utils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:39
 **/
/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 批量执行模型
 *
 * @author fan.tang created on 2017/8/3 下午10:01.
 * @version 1.0
 */
public class BatchExecutor<T> {
    /**
     * 数据集
     */
    private List<T> data;

    /**
     * 每个批次数据量
     */
    private int batchSize;
    /**
     * 记录总数
     */
    private int recordCount;

    /**
     * 批次执行函数
     */
    private Lamb2<T> lamb2;

    public static interface Lamb2<T> {
        void apply(List<T> t);
    }

    public static <T> BatchExecutor<T> create(List<T> data, int batchSize, Lamb2<T> lamb2) {
        return new BatchExecutor<>(data, batchSize, lamb2);
    }

    private BatchExecutor(List<T> data, int batchSize, Lamb2<T> lamb2) {
        this.data = data;
        this.batchSize = batchSize;
        this.recordCount = data.size();
        this.lamb2 = lamb2;
    }

    /**
     * 分批执行
     *
     * @return
     */
    public Integer execute() {
        if (recordCount > batchSize) {
            for (int i = 1; i <= getBatchCount(); i++) {
                lamb2.apply(getCurrentBatchData(i));
            }
        } else {
            lamb2.apply(data);
        }
        return recordCount;
    }

    private List<T> getCurrentBatchData(int currentBatch) {
        if (data == null) {
            return Lists.newArrayList();
        }
        int from = this.getFromIndex(currentBatch);
        int to = this.getToIndex(currentBatch);
        List<T> subData = Lists.newArrayList();
        for (int i = from; i < to; i++) {
            subData.add(data.get(i));
        }
        return subData;
    }

    /**
     * 总批次娄
     *
     * @return
     */
    private int getBatchCount() {
        int size = recordCount / batchSize;
        int mod = recordCount % batchSize;
        if (mod != 0)
            size++;
        return recordCount == 0 ? 1 : size;
    }

    /**
     * 取当前批次的开始
     *
     * @param currentBatch
     * @return
     */
    public int getFromIndex(int currentBatch) {
        return (currentBatch - 1) * batchSize;
    }

    public int getToIndex(int currentBatch) {
        return Math.min(recordCount, currentBatch * batchSize);
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public List<T> getData() {
        return data;
    }
}
