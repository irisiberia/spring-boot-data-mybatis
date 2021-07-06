package com.example.springbootdatamybatis.mozart.excel.export;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:24
 **/
public interface StreamingExporter {

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs) throws IOException, InvalidFormatException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, boolean lockResultSheet) throws IOException, InvalidFormatException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title) throws IOException, InvalidFormatException;

    <T> void export(Iterable<List<T>> dataStream, Class<T> elementClazz, OutputStream writeToOs, String title, int sheetRowMaxSize) throws IOException, InvalidFormatException;
}
