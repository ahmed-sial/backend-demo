package com.example.backenddemo.comment.application;

import com.example.backenddemo.comment.api.dto.CommentResponseDto;
import com.example.backenddemo.comment.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .postId(comment.getPost().getId())
                .depthLevel(comment.getDepthLevel())
                .parentCommentId(comment.getParentComment() != null
                        ? comment.getParentComment().getId() : null)
                .build();
    }
}
