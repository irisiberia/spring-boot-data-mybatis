package com.example.springbootdatamybatis.mozart.excel.beans;

/**
 * Copyright (c) 2017 Wormpex.com. All Rights Reserved.
 */

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流式导出
 *
 * @author fan.tang created on 2017/9/20 下午2:58.
 * @version 1.0
 */
public class StreamingParams<T> {
    /**
     * 导出的文件名
     */
    private String fileName;
    /**
     * 流式导出的数据迭代器
     */
    private Iterable<List<T>> dataStream;
    /**
     * 导出的bean类型
     */
    private Class<T> elementClazz;
    /**
     * 导出的目标response
     */
    private HttpServletResponse response;

    /**
     * true:锁定目标sheet, false:不锁定
     */
    private boolean lockResultSheet;

    public StreamingParams() {
    }

    public String getFileName() {
        return fileName;
    }

    public Iterable<List<T>> getDataStream() {
        return dataStream;
    }

    public Class<T> getElementClazz() {
        return elementClazz;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public boolean isLockResultSheet() {
        return lockResultSheet;
    }

    public StreamingParams<T> withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public StreamingParams<T> withDataStream(Iterable<List<T>> dataStream) {
        this.dataStream = dataStream;
        return this;
    }

    public StreamingParams<T> withElementClazz(Class<T> elementClazz) {
        this.elementClazz = elementClazz;
        return this;
    }

    public StreamingParams<T> withHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
        return this;
    }

    public StreamingParams<T> withResultSheetLock(boolean lockResultSheet) {
        this.lockResultSheet = lockResultSheet;
        return this;
    }
}
