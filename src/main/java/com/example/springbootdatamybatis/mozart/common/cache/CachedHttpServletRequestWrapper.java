package com.example.springbootdatamybatis.mozart.common.cache;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:25
 **/
public class CachedHttpServletRequestWrapper  extends HttpServletRequestWrapper implements CachedStreamEntity {

    private CachedInputStream cachedInputStream;

    public CachedHttpServletRequestWrapper(HttpServletRequest httpServletRequest, int initCacheSize, int maxCacheSize)
            throws IOException {
        super(httpServletRequest);
        this.cachedInputStream = new CachedInputStream(httpServletRequest, initCacheSize, maxCacheSize);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return cachedInputStream;
    }

    @Override
    public CachedStream getCachedStream() {
        return cachedInputStream;
    }

    @Override
    public void flushStream() {
        //do nothing
    }}