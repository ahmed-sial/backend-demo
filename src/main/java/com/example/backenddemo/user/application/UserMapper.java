package com.example.backenddemo.user.application;

import com.example.backenddemo.user.api.dto.UserResponseDto;
import com.example.backenddemo.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public final UserResponseDto toDto(User user) {
        return UserResponseDto
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .isPremium(user.getIsPremium())
                .build();
    }
}
