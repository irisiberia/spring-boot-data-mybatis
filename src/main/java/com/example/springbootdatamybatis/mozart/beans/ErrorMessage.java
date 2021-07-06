package com.example.springbootdatamybatis.mozart.beans;

/**
 * @Author zhouhe
 * @Date 2019-10-04 15:21
 **/
public class ErrorMessage {
    private int code;
    private String errMsg;

    public ErrorMessage() {
    }

    public ErrorMessage(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
