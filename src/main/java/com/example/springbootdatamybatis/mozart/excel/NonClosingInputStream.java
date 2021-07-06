package com.example.springbootdatamybatis.mozart.excel;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:35
 **/
public class NonClosingInputStream extends FilterInputStream {

    public NonClosingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {
    }
}
