package com.example.springbootdatamybatis.mozart.common.json;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:46
 **/
public class KeyValue {
    private Object key;

    private Object value;

    public KeyValue() {
    }

    public KeyValue(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
