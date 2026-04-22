package com.example.backenddemo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BotReplyLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleBotReplyLimitExceeded(
            BotReplyLimitExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(errorBody(ex.getMessage(), 429));
    }

    @ExceptionHandler(BotInteractionsUnderCooldownPeriodException.class)
    public ResponseEntity<Map<String, Object>> handleCooldown(
            BotInteractionsUnderCooldownPeriodException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(errorBody(ex.getMessage(), 429));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePostNotFound(
            PostNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorBody(ex.getMessage(), 404));
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorNotFound(
            AuthorNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorBody(ex.getMessage(), 404));
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<Map<String, Object>> handleUsernameExists(
            UsernameAlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody(ex.getMessage(), 409));
    }

    @ExceptionHandler(BotNameAlreadyExists.class)
    public ResponseEntity<Map<String, Object>> handleBotNameExists(
            BotNameAlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorBody(ex.getMessage(), 409));
    }

    @ExceptionHandler(DepthLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleDepthLimit(
            DepthLimitExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(errorBody(ex.getMessage(), 429));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody("An unexpected error occurred", 500));
    }

    private Map<String, Object> errorBody(String message, int status) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "message", message
        );
    }
}
