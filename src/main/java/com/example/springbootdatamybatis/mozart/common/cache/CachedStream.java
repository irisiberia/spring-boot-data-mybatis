package com.example.springbootdatamybatis.mozart.common.cache;

import java.io.Closeable;

/**
 * 可以在内存中缓存为字节数组的流，主要用于记录流读取或者写入的数据
 *
 * @Author zhouhe
 * @Date 2019-10-04 15:27
 **/
public interface CachedStream extends Closeable {
    /**
     * 返回缓存的字节数据
     *
     * @return
     */
    byte[] getCached();
}
