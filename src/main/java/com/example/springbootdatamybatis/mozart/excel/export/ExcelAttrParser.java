package com.example.springbootdatamybatis.mozart.excel.export;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:23
 **/

import com.example.springbootdatamybatis.mozart.excel.beans.DataFormatter;
import com.example.springbootdatamybatis.mozart.excel.beans.DefaultDataFormatter;
import com.example.springbootdatamybatis.mozart.excel.export.annotations.ExcelCellData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by fan.tang on 2017/6/16.
 */
public class ExcelAttrParser<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelAttrParser.class);
    private Class<T> dataClass;
    private Map<String, String> headerToFiledName = Maps.newLinkedHashMap();
    private Map<String, DataFormatter> fieldToFormatter = Maps.newLinkedHashMap();
    private Map<Integer, String> colCellStyles = Maps.newLinkedHashMap();

    public ExcelAttrParser(Class<T> dataClass) {
        if (dataClass == null) {
            throw new IllegalArgumentException("dataClass is null.");
        }
        this.dataClass = dataClass;
        doParse(this.dataClass);
    }

    private void doParse(Class<T> dataClass) {
        Field[] declaredFields = dataClass.getDeclaredFields();
        int curExcelDataIndex = 0;
        for (Field declaredField : declaredFields) {
            ExcelCellData ecd = declaredField.getAnnotation(ExcelCellData.class);
            if (ecd == null) {
                continue;
            }

            String fieldName = declaredField.getName();
            if (StringUtils.isEmpty(ecd.header())) {
                // 没有设置header的话使用字段名作为默认header
                headerToFiledName.put(fieldName, fieldName);
            } else {
                headerToFiledName.put(ecd.header(), fieldName);
            }

            boolean hasNoSpecFormatter = DefaultDataFormatter.class == ecd.dataFormatter();
            if (!hasNoSpecFormatter) { // 配置了自定义格式转化器
                try {
                    fieldToFormatter.put(fieldName, ecd.dataFormatter().newInstance());
                } catch (Exception e) {
                    LOG.warn("字段{}的格式转换器配置失败", fieldName);
                }
            }

            if (StringUtils.isNotBlank(ecd.cellStyle())) {
                colCellStyles.put(curExcelDataIndex, ecd.cellStyle());
            }
            curExcelDataIndex++;
        }
    }

    public List<String> getHeaders() {
        return Lists.newArrayList(headerToFiledName.keySet());
    }

    public Map<Integer, String> colCellStyles() {
        return Maps.newLinkedHashMap(colCellStyles);
    }

    public List<Object> propertyValuesList(T dataObj) {
        final List<Object> propValues = Lists.newArrayList();
        for (Map.Entry<String, String> entry : headerToFiledName.entrySet()) {
            final String prop = entry.getValue();
            try {
                Object value = PropertyUtils.getProperty(dataObj, prop);
                if (fieldToFormatter.get(prop) != null) {
                    value = fieldToFormatter.get(prop).format(value);
                }
                propValues.add(value);
            } catch (Exception e) {
                String message = "Failed to evaluate property " + prop + " of row object of class "
                        + dataObj.getClass().getName();
                LOG.error(message, e);
                throw new IllegalStateException(message, e);
            }
        }

        return propValues;
    }
}

