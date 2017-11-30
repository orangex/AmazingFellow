package com.orangex.amazingfellow.base;

import com.orangex.amazingfellow.R;

/**
 * Created by chengyuan.wang on 2017/10/30.
 */

public class ApiException extends RuntimeException {
    public static final int code_empty = -1;
    private static final String DEFAULT_DISPLAY_MESSAGE = AFApplication.getAppContext().getString(R.string.error_server_default);
    private int code;
    private String message;
    private String displayMessage;
    
    public ApiException(String message, int code, String displayMessage) {
        
        this.code = code;
        this.message = message;
        this.displayMessage = displayMessage;
    }
    public ApiException(String message,int code ) {
        new ApiException(message, code, DEFAULT_DISPLAY_MESSAGE);
    }
    
    public ApiException(String message) {
        new ApiException(message, code_empty);
    }

    @Override
    public String getMessage() {
        return code + " " + message;
    }

    public int getCode() {
        return code;
    }
    
    public String getDisplayMessage() {
        return displayMessage;
    }
    
    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
