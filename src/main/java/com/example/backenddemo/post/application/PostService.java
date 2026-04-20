package com.example.backenddemo.post.application;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.author.infrastructure.persistence.AuthorRepository;
import com.example.backenddemo.common.exception.AuthorNotFoundException;
import com.example.backenddemo.common.exception.PostNotFoundException;
import com.example.backenddemo.post.api.dto.PostRequestDto;
import com.example.backenddemo.post.api.dto.PostResponseDto;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final PostMapper postMapper;

    public PostResponseDto create(PostRequestDto postDto) {
        Author author = authorRepository.findById(postDto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        var post = Post
                .builder()
                .author(author)
                .content(postDto.content())
                .likes(0L)
                .build();

        var savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    public PostResponseDto likePost(UUID postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

}
