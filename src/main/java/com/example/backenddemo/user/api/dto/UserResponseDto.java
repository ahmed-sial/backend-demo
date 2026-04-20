package com.example.backenddemo.user.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID userId,
        String username,
        Boolean isPremium
) {
}
