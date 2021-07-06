package com.example.springbootdatamybatis.mozart.common.cache;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:27
 **/
public interface CachedStreamEntity {
    CachedStream getCachedStream();
    void flushStream();
}
