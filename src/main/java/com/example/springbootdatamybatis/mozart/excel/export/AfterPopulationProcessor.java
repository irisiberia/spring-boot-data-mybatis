package com.example.springbootdatamybatis.mozart.excel.export;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:23
 **/
public interface AfterPopulationProcessor {
    void postProcess(Sheet sheet);
}
