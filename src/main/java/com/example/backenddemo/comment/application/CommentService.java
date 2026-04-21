package com.example.backenddemo.comment.application;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.author.infrastructure.persistence.AuthorRepository;
import com.example.backenddemo.bot.domain.Bot;
import com.example.backenddemo.comment.api.dto.CommentRequestDto;
import com.example.backenddemo.comment.api.dto.CommentResponseDto;
import com.example.backenddemo.comment.domain.Comment;
import com.example.backenddemo.comment.infrastructure.persistence.CommentRepository;
import com.example.backenddemo.common.utils.Utils;
import com.example.backenddemo.common.exception.AuthorNotFoundException;
import com.example.backenddemo.common.exception.PostNotFoundException;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.common.infrastructure.cache.RedisService;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
import com.example.backenddemo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper;
    private final RedisService redisService;

    public CommentResponseDto create(UUID postId, CommentRequestDto commentDto) {
        Author author = authorRepository.findById(commentDto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Utils.isInteractionValidForBot(post, author, redisService);
        var comment = Comment
                .builder()
                .post(post)
                .author(author)
                .content(commentDto.content())
                .build();

        var savedComment = commentRepository.save(comment);
        if (author instanceof User) {
            redisService.updateViralityByHumanComment(author.getId());
        } else if (author instanceof Bot) {
            redisService.updateViralityByBot(author.getId());
            redisService.addCooldownKey(author.getId(), post.getAuthor().getId());
        }
        return commentMapper.toDto(savedComment);
    }


}
