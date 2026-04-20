package com.example.backenddemo.post.application;

import com.example.backenddemo.post.api.dto.PostResponseDto;
import com.example.backenddemo.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostResponseDto toDto(Post post) {
        return PostResponseDto
                .builder()
                .postId(post.getId())
                .authorId(post.getAuthor().getId())
                .content(post.getContent())
                .likes(post.getLikes())
                .build();
    }
}
