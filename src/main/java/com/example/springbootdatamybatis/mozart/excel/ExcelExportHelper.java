package com.example.springbootdatamybatis.mozart.excel;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:33
 **/

import com.example.springbootdatamybatis.mozart.excel.beans.Context;
import com.example.springbootdatamybatis.mozart.excel.beans.StreamingParams;
import com.example.springbootdatamybatis.mozart.excel.export.StreamingExporter;
import com.example.springbootdatamybatis.mozart.excel.export.TemplateExporter;
import com.example.springbootdatamybatis.mozart.excel.export.impl.SimpleStreamingExporterImpl;
import com.example.springbootdatamybatis.mozart.excel.export.impl.TemplateExporterImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel流式导出
 *
 * Created by fan.tang on 2017/6/15.
 */
public class ExcelExportHelper {

    /**
     * 使用流式导出的方式导出Excel
     *
     * @param <T> 待导出的对象
     *
     * @param fileName 导出文件名
     * @param dataStream 使用迭代的方式批量获得导出的数据
     * @param elementClazz 导出元素的class对象
     * @param response 导出的目标输出流
     * @param request
     * @throws Exception
     */
    public static <T> void exportToHttpResp(String fileName, Iterable<List<T>> dataStream, Class<T> elementClazz,
                                            HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        //IE内核的使用UTF-8编码
        if (StringUtils.containsIgnoreCase(userAgent, "MSIE" ) || StringUtils.containsIgnoreCase(userAgent, "Trident")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("application/vnd.ms-excel");

        StreamingExporter exporter = new SimpleStreamingExporterImpl();
        exporter.export(dataStream, elementClazz, response.getOutputStream());
    }

    /**
     * 使用流式导出的方式导出Excel
     *
     * @param <T> 待导出的对象
     *
     * @param fileName 导出文件名
     * @param dataStream 使用迭代的方式批量获得导出的数据
     * @param elementClazz 导出元素的class对象
     * @param response 导出的目标输出流 @throws Exception
     */
    public static <T> void exportToHttpResp(String fileName, Iterable<List<T>> dataStream, Class<T> elementClazz,
                                            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel");

        StreamingExporter exporter = new SimpleStreamingExporterImpl();
        exporter.export(dataStream, elementClazz, response.getOutputStream());
    }

    /**
     * 使用流式导出的方式导出Excel到Http输出流
     *
     * @param params 流式导出对应的参数
     * @param <T> 导出的元素的类
     */
    public static <T> void exportToHttpResp(StreamingParams<T> params) throws Exception {
        final HttpServletResponse response = params.getResponse();
        final String fileName = params.getFileName();
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel");
        StreamingExporter exporter = new SimpleStreamingExporterImpl();
        exporter.export(params.getDataStream(), params.getElementClazz(), params.getResponse().getOutputStream(),
                params.isLockResultSheet());
    }

    /**
     * 使用模板导出的方式导出Excel到Http输出流， 注意：此方式不支持流式导出，需要注意导出的数据量不能过大，防止内存溢出
     *
     * @param fileName 导出的文件名
     * @param context 导出文件所需的上下文
     * @param response httpServletResponse，导出的目标输出流
     * @throws Exception
     */
    public static void exportToHttpRespWithTemplate(String fileName, Context context, HttpServletResponse response)
            throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel");

        TemplateExporter templateExporter = new TemplateExporterImpl();
        templateExporter.export(context, response.getOutputStream());
    }

    /**
     * 使用模板导出的方式导出Excel到目标输出流， 注意：此方式不支持流式导出，需要注意导出的数据量不能过大，防止内存溢出
     *
     * @param context 导出文件所需的上下文
     * @param os 导出的目标输出流
     * @throws IOException
     */
    public static void exportTo(Context context, OutputStream os) throws IOException {
        new TemplateExporterImpl().export(context, os);
    }
}
