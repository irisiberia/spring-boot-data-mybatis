package com.example.springbootdatamybatis.mozart.common.cache;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:26
 **/
public class CachedOutputStream extends ServletOutputStream implements CachedStream {
    private ByteArrayOutputStream cachedOutputStream;
    private ServletOutputStream srcOutputStream;
    private int maxCacheSize;

    public CachedOutputStream(ServletOutputStream srcOutputStream, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.srcOutputStream = srcOutputStream;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public byte[] getCached() {
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void write(int b) throws IOException {
        this.srcOutputStream.write(b);
        if(this.cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.cachedOutputStream.close();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
