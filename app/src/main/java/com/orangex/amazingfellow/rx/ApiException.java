package com.orangex.amazingfellow.rx;

/**
 * Created by chengyuan.wang on 2017/10/30.
 */

public class ApiException extends RuntimeException {
    private int code;
    private String message;


    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return code + " " + message;
    }

    public int getCode() {
        return code;
    }
}
