package com.example.springbootdatamybatis.mozart.common.json;

import org.springframework.core.NestedRuntimeException;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:47
 **/
public class JsonException  extends NestedRuntimeException {

    private static final long serialVersionUID = -9198606590046525595L;

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
