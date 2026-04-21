package com.example.backenddemo.common.exception;

public class BotInteractionsUnderCooldownPeriodException extends RuntimeException {
    public BotInteractionsUnderCooldownPeriodException(String message) {
        super(message);
    }
}
