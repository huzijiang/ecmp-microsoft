package com.hq.ecmp.constant.enumerate;

/**
 *
 * @author yangnan
 */
public enum ErrorCodeEnum {
    SUCCESS(0, "success"),
    ILLEGAL_ARGUMENT_ERROR(10000, "无效参数"),
    SYSTEM_DEFAULT_ERROR(1, "网络异常,请您重试!");

    private int code;
    private String msg;

    private ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
