package com.example.backenddemo.bot.application;

import com.example.backenddemo.bot.infrastructure.persistence.BotJpaRepository;
import com.example.backenddemo.bot.api.dto.BotRequestDto;
import com.example.backenddemo.bot.api.dto.BotResponseDto;
import com.example.backenddemo.bot.domain.Bot;
import com.example.backenddemo.common.exception.BotNameAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final BotJpaRepository botRepository;
    private final BotMapper botMapper;

    public BotResponseDto create(BotRequestDto botDto) {
        var nameExists = botRepository.existsByName(botDto.name());
        if (nameExists)
            throw new BotNameAlreadyExists("Bot name already exists");

        var bot = Bot
                .builder()
                .name(botDto.name())
                .personaDescription(botDto.personaDescription())
                .posts(null)
                .comments(null)
                .build();

        var savedUser = botRepository.save(bot);
        return botMapper.toDto(savedUser);
    }
}
