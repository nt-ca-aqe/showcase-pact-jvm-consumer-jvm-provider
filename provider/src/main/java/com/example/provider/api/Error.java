package com.example.provider.api;

/**
 * Defines the error format.
 */
public class Error {
    private Integer status;

    private String error;

    private String message;

    public Error(Integer status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
