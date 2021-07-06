package com.example.springbootdatamybatis.mozart.common.mosaic;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:38
 **/
public class UriMatcher {

    /**
     * 判断一个URI是否是匹配一个模式
     * 支持多个URI，模式之间用,分割
     * 每个URI支持最后*号匹配
     *
     * @param pattern  模式字符串
     * @param uri  待匹配的URI
     * @return
     */
    public static boolean match(String pattern, String uri){
        if(StringUtils.isBlank(pattern)){
            return false;
        }

        String[] urls = StringUtils.split(pattern, ",");
        if(urls.length == 0){
            return false;
        }

        for(String url: urls){
            url = StringUtils.trimToEmpty(url);
            if(url.endsWith("*")){
                String sub = url.substring(0, url.length() - 1);
                if(uri.startsWith(sub)){
                    return true;
                }
            }else{
                if(uri.equals(url)){
                    return true;
                }
            }
        }

        return false;
    }
}
