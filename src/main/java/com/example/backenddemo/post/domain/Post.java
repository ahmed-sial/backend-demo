package com.example.backenddemo.post.domain;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String content;
    private Long likes;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
