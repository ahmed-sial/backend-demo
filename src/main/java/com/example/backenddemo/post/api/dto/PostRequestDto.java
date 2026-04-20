package com.example.backenddemo.post.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PostRequestDto(
        UUID authorId,
        @NotBlank(message = "Post's content can't be null or blank")
        String content
) {}
