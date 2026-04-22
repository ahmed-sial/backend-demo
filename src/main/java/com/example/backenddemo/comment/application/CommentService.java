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
import com.example.backenddemo.notification.NotificationService;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.common.infrastructure.cache.RedisService;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
import com.example.backenddemo.user.domain.User;
import jakarta.transaction.Transactional;
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
    private final NotificationService notificationService;

    @Transactional
    public CommentResponseDto create(UUID postId, CommentRequestDto commentDto) {
        Author author = authorRepository.findById(commentDto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        int depthLevel = 0;
        Comment parentComment = null;

        if (commentDto.parentCommentId() != null) {
            parentComment = commentRepository
                    .findById(commentDto.parentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            Integer parentDepth = redisService.getCommentDepth(parentComment.getId());
            if (parentDepth == null) {
                parentDepth = parentComment.getDepthLevel();
            }
            depthLevel = parentDepth + 1;
        }

        redisService.enforceDepthCap(depthLevel);

        var comment = Comment
                .builder()
                .post(post)
                .author(author)
                .content(commentDto.content())
                .depthLevel(depthLevel)
                .build();

        if (author instanceof Bot) {
            Utils.isInteractionValidForBot(post, author, redisService);
            redisService.updateViralityByBot(postId);
        }
        var savedComment = commentRepository.save(comment);
        redisService.setCommentDepth(savedComment.getId(), depthLevel);
        if (author instanceof Bot) {
            notificationService.sendNotification(post.getAuthor().getId());
        } else if (author instanceof User) {
            redisService.updateViralityByHumanComment(postId);
        }
        return commentMapper.toDto(savedComment);
    }
}
