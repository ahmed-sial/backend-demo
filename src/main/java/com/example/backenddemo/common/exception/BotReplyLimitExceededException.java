package com.example.backenddemo.common.exception;

public class BotReplyLimitExceededException extends RuntimeException {
    public BotReplyLimitExceededException(String message) {
        super(message);
    }
}
