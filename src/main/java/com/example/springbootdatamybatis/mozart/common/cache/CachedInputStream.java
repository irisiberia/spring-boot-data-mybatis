package com.example.springbootdatamybatis.mozart.common.cache;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:26
 **/
public class CachedInputStream extends ServletInputStream implements CachedStream{
    private ByteArrayOutputStream cachedOutputStream;
    private HttpServletRequest request;
    private int maxCacheSize;

    public CachedInputStream(HttpServletRequest request, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.request = request;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public int read() throws IOException {
        int b = request.getInputStream().read();
        if(b != -1 && cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
        return b;
    }

    @Override
    public byte[] getCached(){
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void close() throws IOException {
        super.close();
        cachedOutputStream.close();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}
