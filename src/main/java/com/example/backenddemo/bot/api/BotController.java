package com.example.backenddemo.bot.api;

import com.example.backenddemo.bot.api.dto.BotRequestDto;
import com.example.backenddemo.bot.api.dto.BotResponseDto;
import com.example.backenddemo.bot.application.BotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bots")
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;

    @PostMapping
    public ResponseEntity<BotResponseDto> create(
            @Valid
            @RequestBody
            BotRequestDto botRequestDto
    ) {
       var savedUser = botService.create(botRequestDto);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(savedUser);
    }
}
