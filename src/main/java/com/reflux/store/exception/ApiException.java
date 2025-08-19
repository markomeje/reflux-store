package com.reflux.store.exception;

public class ApiException extends RuntimeException {
    String message;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }
}
