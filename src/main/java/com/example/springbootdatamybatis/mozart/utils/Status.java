package com.example.springbootdatamybatis.mozart.utils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 18:07
 **/

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;

/**
 * Biz状态码。
 *
 * @author Daniel Li
 * @since 25 May 2017
 */
public class Status implements Serializable, Comparable<Status> {

    public static final int ERROR = -1024;

    public static final int SUCCESS = 0;

    private static final long serialVersionUID = 7569524956130901222L;

    private final int status;

    private final String reason;

    @JsonCreator
    protected Status(@JsonProperty("status") int status, @JsonProperty("reason") String reason) {
        this.status = status;
        this.reason = reason;
    }

    public static Status create(int status, String reason) {
        return new Status(status, reason);
    }

    public static Status success() {
        return create(SUCCESS, null);
    }

    public static Status error(String reason) {
        return create(ERROR, reason);
    }

    public static Status error(int status, String reason) {
        Assert.isTrue(status < SUCCESS, "status must be less than 0");
        return create(status, reason);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return status >= SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public StatusCodeException asError() {
        return new StatusCodeException(this);
    }

    public StatusCodeException asError(String message) {
        return new StatusCodeException(message, this);
    }

    public StatusCodeException asError(String message, Throwable cause) {
        return new StatusCodeException(message, cause, this);
    }

    public StatusCodeException asError(Throwable cause) {
        return new StatusCodeException(cause, this);
    }

    @Override
    public int compareTo(Status o) {
        return status - o.status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;

        Status status1 = (Status) o;

        if (status != status1.status) return false;
        return reason != null ? reason.equals(status1.reason) : status1.reason == null;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder(5)
                .append(status)
                .append(' ')
                .append(reason)
                .toString();
    }
}
