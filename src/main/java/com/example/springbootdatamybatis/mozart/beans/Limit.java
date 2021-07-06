package com.example.springbootdatamybatis.mozart.beans;

/**
 * @Author he.zhou
 * @Date 2019-10-04 17:01
 **/

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分页。
 *
 * @author Daniel Li
 * @since 20 August 2016
 */
public class Limit implements Serializable {

    protected final int page;

    protected final int offset;

    protected final int size;

    @JsonCreator
    protected Limit(@JsonProperty("page") int page, @JsonProperty("offset") int offset, @JsonProperty("size") int size) {
        this.page = page;
        this.offset = offset;
        this.size = size;
    }

    public static Limit createByOffset(int offset, int size) {
        return new Limit((offset / size) + 1, offset, size);
    }

    public static Limit createByPage(int page, int size) {
        return new Limit(page, (page - 1) * size, size);
    }

    public int getPage() {
        return page;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Limit)) return false;

        Limit limit = (Limit) o;

        if (page != limit.page) return false;
        return size == limit.size;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "Limit{" +
                "page=" + page +
                ", offset=" + offset +
                ", size=" + size +
                '}';
    }
}
