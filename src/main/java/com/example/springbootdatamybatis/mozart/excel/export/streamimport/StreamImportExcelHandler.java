package com.example.springbootdatamybatis.mozart.excel.export.streamimport;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:26
 **/


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.collect.Lists;
/**
 *
 * 流式 读取处理
 *
 * @author yunan.zheng
 * @since 04 九月 2017
 */
@Component
public class StreamImportExcelHandler {
    private Logger logger = LoggerFactory.getLogger(StreamImportExcelHandler.class);
    /**
     * 流式 读取数据
     *
     * @param excelFile
     * @param resultHandler
     */
    public void handler(MultipartFile excelFile, StreamImportExcelResultHandler resultHandler) {
        try (InputStream inputStream = excelFile.getInputStream()) {
            int handlerSize = 1;
            if (resultHandler.getHandlerSize() != null) {
                handlerSize = resultHandler.getHandlerSize().intValue();
            }
            // 流式读取
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(500)// number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096) // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(inputStream);
            for (Sheet sheet : workbook) {
                int rowSize = 0;
                List<Row> resultList = Lists.newArrayListWithCapacity(handlerSize);
                for (Row row : sheet) {
                    resultList.add(row);
                    rowSize++;
                    if (handlerSize == rowSize) {
                        resultHandler.resultHandler(sheet.getSheetName(), resultList);
                        resultList.clear();
                        rowSize = 0;
                    }
                }
                if (CollectionUtils.isNotEmpty(resultList)){
                    resultHandler.resultHandler(sheet.getSheetName(), resultList);
                }
            }
        } catch (IOException e) {
            logger.error("{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}