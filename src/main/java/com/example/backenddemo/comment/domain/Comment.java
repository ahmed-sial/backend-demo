package com.example.backenddemo.comment.domain;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.common.domain.BaseEntity;
import com.example.backenddemo.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String content;
    @Setter(AccessLevel.NONE)
    private Integer depthLevel;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", nullable = false)
    private Comment parentComment;
}
