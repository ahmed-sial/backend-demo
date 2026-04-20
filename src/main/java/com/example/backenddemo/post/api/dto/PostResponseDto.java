package com.example.backenddemo.post.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PostResponseDto(
        UUID postId,
        UUID authorId,
        String content,
        Long likes
) {
}
