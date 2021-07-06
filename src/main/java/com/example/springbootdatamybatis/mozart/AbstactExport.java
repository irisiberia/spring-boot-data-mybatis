package com.example.springbootdatamybatis.mozart;

import com.example.springbootdatamybatis.mozart.excel.ExcelExportHelper;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author he.zhou
 * @Date 2019-10-04 17:11
 **/
@Slf4j
@Component
public class AbstactExport {
    /**
     * 流式导出
     *
     * @param outFileName
     * @param dataStream
     * @param response
     */
    public <T> void download(String outFileName, Iterable<List<T>> dataStream, Class<T> elementClazz
            , HttpServletResponse response) {
        Stopwatch stp = Stopwatch.createStarted();
        try {
            ExcelExportHelper.exportToHttpResp(outFileName, dataStream, elementClazz, response);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("流式导出失败:{}", outFileName, e);

        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        log.info("流式导出耗时:{} ms", stp.elapsed(TimeUnit.MILLISECONDS));
    }
}
