package com.example.backenddemo.post.infrastructure.persistence;

import com.example.backenddemo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
