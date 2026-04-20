package com.example.backenddemo.comment.application;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.author.infrastructure.persistence.AuthorRepository;
import com.example.backenddemo.comment.api.dto.CommentRequestDto;
import com.example.backenddemo.comment.api.dto.CommentResponseDto;
import com.example.backenddemo.comment.domain.Comment;
import com.example.backenddemo.comment.infrastructure.persistence.CommentRepository;
import com.example.backenddemo.common.exception.AuthorNotFoundException;
import com.example.backenddemo.common.exception.PostNotFoundException;
import com.example.backenddemo.post.domain.Post;
import com.example.backenddemo.post.infrastructure.persistence.PostRepository;
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

    public CommentResponseDto create(UUID postId, CommentRequestDto commentDto) {
        Author author = authorRepository.findById(commentDto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        var comment = Comment
                .builder()
                .post(post)
                .author(author)
                .content(commentDto.content())
                .build();

        var savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }
}
