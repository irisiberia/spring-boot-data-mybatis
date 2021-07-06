package com.example.springbootdatamybatis.mozart.excel.beans;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:06
 **/

import com.example.springbootdatamybatis.mozart.excel.NonClosingInputStream;
import com.example.springbootdatamybatis.mozart.excel.export.AfterPopulationProcessor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.jxls.command.CellDataUpdater;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fan.tang on 2017/6/16.
 */
@NoArgsConstructor
public class Context {
    /**
     * 存放模板占位符和值
     */
    private final Map<String, Object> templateValueMap = Maps.newHashMap();

    /**
     * 支持使用CellUpdater动态更新cell
     */
    private final Map<String, CellDataUpdater> cellDataUpdaterMap = Maps.newHashMap();

    private InputStream templateInputStream;

    /**
     * 需要加锁保护的sheetName
     */
    private final Set<String> sheetNamesToLock = Sets.newHashSet();

    /**
     * 需要加锁保护的sheetIndex, sheet索引下标从0开始计数，模板sheet不计入下标
     */
    private final Set<Integer> sheetIndexToLock = Sets.newHashSet();

    /**
     * 数据填充后，写入输出流前触发的处理器，支持对Sheet进行额外的处理
     */
    private final List<AfterPopulationProcessor> afterProcessors = Lists.newArrayList();

    /**
     * 注意关闭输入流
     *
     * @param templateInputStream
     */
    public Context(InputStream templateInputStream) {
        this.templateInputStream = new NonClosingInputStream(templateInputStream);
    }

    public void setTemplateInputStream(InputStream stream) {
        this.templateInputStream = stream;
    }

    public Map<String, Object> putVar(String varName, Object value) {
        templateValueMap.put(varName, value);
        return templateValueMap;
    }

    public Map<String, CellDataUpdater> putUpdater(String updaterRefName, CellDataUpdater cellDataUpdater) {
        cellDataUpdaterMap.put(updaterRefName, cellDataUpdater);
        return cellDataUpdaterMap;
    }

    public Context withSheetsToLock(Set<String> sheets) {
        if(CollectionUtils.isNotEmpty(sheets)) {
            sheetNamesToLock.addAll(sheets);
        }
        return this;
    }

    public Context withSheetIndexToLock(Set<Integer> sheetIndex) {
        if(CollectionUtils.isNotEmpty(sheetIndex)) {
            sheetIndexToLock.addAll(sheetIndex);
        }
        return this;
    }

    public Context withCellUpdater(String updaterName, CellDataUpdater cellUpdater) {
        cellDataUpdaterMap.put(updaterName, cellUpdater);
        return this;
    }

    public Context withTemplateVar(String varName, Object value) {
        templateValueMap.put(varName, value);
        return this;
    }

    public Context withTemplateInputStream(InputStream inputStream) {
        this.templateInputStream = inputStream;
        return this;
    }

    public Context addPostProcessor(AfterPopulationProcessor processor) {
        this.afterProcessors.add(processor);
        return this;
    }

    public List<AfterPopulationProcessor> getAfterProcessors() {
        return afterProcessors;
    }

    public Map<String, Object> getTemplateValueMap() {
        return templateValueMap;
    }

    public Map<String, CellDataUpdater> getCellDataUpdaterMap() {
        return cellDataUpdaterMap;
    }

    public InputStream getTemplateInputStream() {
        return templateInputStream;
    }

    public Set<String> getSheetNamesToLock() {
        return sheetNamesToLock;
    }

    public Set<Integer> getSheetIndexToLock() {
        return sheetIndexToLock;
    }
}
