package com.example.springbootdatamybatis.mozart.excel.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author he.zhou
 * @Date 2019-10-04 16:07
 **/
public  abstract class DataFormatter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Object format(Object value) {
        if (accept(value)) {
            return apply(value);
        } else {
            logger.debug("value {} 对应的类型不支持格式化", value);
            return value;
        }
    }

    protected abstract boolean accept(Object value);

    protected abstract Object apply(Object value);
}

