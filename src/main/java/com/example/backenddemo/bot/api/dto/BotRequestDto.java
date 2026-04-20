package com.example.backenddemo.bot.api.dto;

import jakarta.validation.constraints.NotBlank;

public record BotRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String personaDescription
) {
}
