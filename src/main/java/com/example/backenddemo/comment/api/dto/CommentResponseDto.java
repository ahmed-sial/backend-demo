package com.example.backenddemo.comment.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentResponseDto (
        UUID commentId,
        UUID authorId,
        UUID postId,
        String content,
        Integer depthLevel,
        UUID parentCommentId
){
}
