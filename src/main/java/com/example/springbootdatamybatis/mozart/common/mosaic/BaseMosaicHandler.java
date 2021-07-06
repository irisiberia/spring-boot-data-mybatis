package com.example.springbootdatamybatis.mozart.common.mosaic;

import java.util.Map;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:29
 **/
public abstract class BaseMosaicHandler implements MosaicHandler {

    @Override
    public Map handleParameters(Map params) {
        return params;
    }

    @Override
    public String handleResponse(String uri, String body) {
        return body;
    }

    @Override
    public String handleRequest(String uri, String body) {
        return body;
    }
}
