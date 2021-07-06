package com.example.springbootdatamybatis.mozart.excel;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:36
 **/
public class NonClosingOutputStream extends FilterOutputStream {
    public NonClosingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(byte[] b, int off, int let) throws IOException {
        out.write(b, off, let);
    }

    @Override
    public void close() throws IOException {
    }
}