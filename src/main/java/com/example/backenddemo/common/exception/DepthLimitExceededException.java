package com.example.backenddemo.common.exception;

public class DepthLimitExceededException extends RuntimeException {
    public DepthLimitExceededException(String message) {
        super(message);
    }
}
