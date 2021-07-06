package com.example.springbootdatamybatis.mozart.common.mosaic;

import java.util.Map;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:29
 **/

/**
 * 把一些敏感信息打上马赛克，避免泄露敏感数据<br/>
 * 这些都只会对日志内容生效,不会对真正的请求/响应数据产生影响!
 */
public interface MosaicHandler {
    /***
     * 处理请求的参数,这个是对于参数是key-value这种形式的处理
     */
    Map handleParameters(Map params);

    /***
     * 处理请求体,这个可以理解为是对@RequestBody形式的参数处理
     */
    String handleRequest(String uri, String body);

    /***
     * 处理返回的结果
     */
    String handleResponse(String uri, String body);
}
