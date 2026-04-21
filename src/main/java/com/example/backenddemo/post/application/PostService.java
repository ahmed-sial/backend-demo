package com.example.backenddemo.post.application;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.author.infrastructure.persistence.AuthorRepository;
import com.example.backenddemo.bot.domain.Bot;
import com.example.backenddemo.common.utils.Utils;
import com.example.backenddemo.common.exception.AuthorNotFoundException;
import com.example.backenddemo.common.exception.PostNotFoundException;
import com.example.backenddemo.common.infrastructure.cache.RedisService;
import com.example.backenddemo.post.api.dto.PostRequestDto;
import com.example.backenddemo.post.api.dto.PostResponseDto;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
import com.example.backenddemo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final PostMapper postMapper;
    private final RedisService redisService;

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
    public PostResponseDto likePost(UUID authorId, UUID postId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Utils.isInteractionValidForBot(post, author, redisService);

        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        if (author instanceof User) {
            redisService.updateViralityByHumanLike(author.getId());
        } else if (author instanceof Bot) {
            redisService.updateViralityByBot(author.getId());
            redisService.addCooldownKey(author.getId(), post.getAuthor().getId());
        }
        return postMapper.toDto(post);
    }

}
