package com.example.springbootdatamybatis.mozart.common.json;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:54
 **/

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;

/**
 * {@link org.joda.time.DateTime} 序列化器。
 *
 * @author Daniel Li
 * @since 12 June 2017
 */
public class DateTimeSerializer extends com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer {

    private boolean custom = false;

    public DateTimeSerializer(JacksonJodaDateFormat format, boolean custom) {
        super(format);
        this.custom = custom;
    }

    @Override
    public com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer withFormat(JacksonJodaDateFormat formatter) {
        return new DateTimeSerializer(formatter, true);
    }

    @Override
    protected boolean _useTimestamp(SerializerProvider provider) {
        if (custom) {
            return false;
        }
        return super._useTimestamp(provider);
    }
}

