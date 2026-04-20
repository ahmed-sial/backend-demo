package com.example.backenddemo.user.application;

import com.example.backenddemo.common.exception.UsernameAlreadyExists;
import com.example.backenddemo.user.api.dto.UserRequestDto;
import com.example.backenddemo.user.api.dto.UserResponseDto;
import com.example.backenddemo.user.domain.User;
import com.example.backenddemo.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto create(UserRequestDto userDto) {
        var usernameExists = userRepository.existsByUsername(userDto.username());
        if (usernameExists)
            throw new UsernameAlreadyExists("Username already exists");

        var user = User
                .builder()
                .username(userDto.username())
                .isPremium(userDto.isPremium())
                .posts(null)
                .comments(null)
                .build();

        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
