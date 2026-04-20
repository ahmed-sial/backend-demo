package com.example.backenddemo.common.exception;

public class BotNameAlreadyExists extends RuntimeException {
    public BotNameAlreadyExists(String message) {
        super(message);
    }
}
