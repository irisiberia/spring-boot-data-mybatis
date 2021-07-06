package com.example.springbootdatamybatis.mozart.common;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:44
 **/

import java.io.IOException;
import java.util.Map;

import com.example.springbootdatamybatis.mozart.common.json.KeyValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Common Framework支持Module。
 *
 * @author Daniel Li
 * @since 11 June 2017
 */
public class CommonFrameworkModule extends SimpleModule {

    public CommonFrameworkModule() {
        super("CommonFrameworkModule", Version.unknownVersion());

        // 兼容 jackson 2.5 以下的版本, 对 Map.Entry 序列化做特殊处理
        addSerializer(Map.Entry.class, new JsonSerializer<Map.Entry>() {
            @Override
            public void serialize(Map.Entry entry, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeObject(new KeyValue(entry.getKey(), entry.getValue()));
            }
        });
    }
}

