package com.example.springbootdatamybatis.mozart.excel.export.streamimport;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:28
 **/

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 流式导入结果处理
 *
 * @author yunan.zheng
 * @since 04 九月 2017
 */
public interface StreamImportExcelResultHandler {
    void resultHandler(String sheetName, List<Row> rows);

    Integer getHandlerSize();
}