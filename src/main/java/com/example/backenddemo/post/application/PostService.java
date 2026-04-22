package com.example.backenddemo.post.application;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.author.infrastructure.persistence.AuthorRepository;
import com.example.backenddemo.bot.domain.Bot;
import com.example.backenddemo.common.utils.Utils;
import com.example.backenddemo.common.exception.AuthorNotFoundException;
import com.example.backenddemo.common.exception.PostNotFoundException;
import com.example.backenddemo.common.infrastructure.cache.RedisService;
import com.example.backenddemo.notification.NotificationService;
import com.example.backenddemo.post.api.dto.PostRequestDto;
import com.example.backenddemo.post.api.dto.PostResponseDto;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
import com.example.backenddemo.user.domain.User;
import jakarta.transaction.Transactional;
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
    private final NotificationService notificationService;

    @Transactional
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

    @Transactional
    public PostResponseDto likePost(UUID authorId, UUID postId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        post.setLikes(post.getLikes() + 1);

        if (author instanceof Bot) {
            Utils.isInteractionValidForBot(post, author, redisService);
            redisService.updateViralityByBot(postId);
        }
        var savedPost = postRepository.save(post);
        if (author instanceof Bot) {
            notificationService.sendNotification(post.getAuthor().getId());
        } else if (author instanceof User) {
            redisService.updateViralityByHumanLike(postId);
        }
        return postMapper.toDto(savedPost);
    }

}
