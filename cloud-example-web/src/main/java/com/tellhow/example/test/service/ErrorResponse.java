package com.tellhow.example.test.service;

/**
 * 请添加类的描述信息.
 *
 * @author yanfei
 * @history 2024/11/18 16:57 yanfei 新建
 * @since JDK1.8
 */
public class ErrorResponse {

    private String errorCode;
    private String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
