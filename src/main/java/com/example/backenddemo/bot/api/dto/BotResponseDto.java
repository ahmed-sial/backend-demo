package com.example.backenddemo.bot.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BotResponseDto(
        UUID botId,
        String name,
        String personaDescription
) {
}
