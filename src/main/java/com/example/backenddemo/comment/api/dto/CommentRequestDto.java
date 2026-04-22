package com.example.backenddemo.comment.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CommentRequestDto(
        UUID authorId,
        @NotBlank(message = "Content can't be null or blank")
        String content,
        UUID parentCommentId
) {
}
