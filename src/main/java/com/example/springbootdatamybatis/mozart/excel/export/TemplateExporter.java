package com.example.springbootdatamybatis.mozart.excel.export;

import com.example.springbootdatamybatis.mozart.excel.beans.Context;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:25
 **/
public interface TemplateExporter {
    void export(Context context, OutputStream os) throws IOException;
}
