package com.example.backenddemo.user.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank
        String username,
        Boolean isPremium
) {
}
