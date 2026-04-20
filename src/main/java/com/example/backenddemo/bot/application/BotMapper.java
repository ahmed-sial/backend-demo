package com.example.backenddemo.bot.application;

import com.example.backenddemo.bot.api.dto.BotResponseDto;
import com.example.backenddemo.bot.domain.Bot;
import org.springframework.stereotype.Component;

@Component
public class BotMapper {
    public BotResponseDto toDto(Bot bot) {
        return BotResponseDto
                .builder()
                .botId(bot.getId())
                .name(bot.getName())
                .personaDescription(bot.getPersonaDescription())
                .build();
    }
}
