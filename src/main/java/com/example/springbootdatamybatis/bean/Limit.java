package com.example.springbootdatamybatis.bean;

public class Limit {
    public static final int DEFAULT_COUNT = 20;
    private static final long serialVersionUID = -6118678253664985760L;
    private int offset;
    private int count;

    public Limit() {
        this(20);
    }

    public Limit(int count) {
        this(0, count);
    }

    public Limit(int offset, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("limit count must be positive.");
        } else if (offset < 0) {
            throw new IllegalArgumentException("limit offset must be positive.");
        } else if (count > 200) {
            throw new IllegalArgumentException("limit count must less than 200");
        } else {
            this.count = count;
            this.offset = offset;
        }
    }

    public String toString() {
        return " Limit " + this.offset + "," + this.count;
    }

    public String asSqlPart() {
        return this.offset + ", " + this.count;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
