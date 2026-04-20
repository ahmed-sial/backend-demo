package com.example.backenddemo.user.api;

import com.example.backenddemo.user.api.dto.UserRequestDto;
import com.example.backenddemo.user.api.dto.UserResponseDto;
import com.example.backenddemo.user.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(
            @Valid
            @RequestBody
            UserRequestDto userRequestDto
    ) {
       var savedUser = userService.create(userRequestDto);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(savedUser);
    }
}
