package com.example.backenddemo.comment.infrastructure.persistence;

import com.example.backenddemo.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
