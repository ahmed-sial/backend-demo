package com.example.backenddemo.user.domain;

import com.example.backenddemo.author.domain.Author;
import com.example.backenddemo.comment.domain.Comment;
import com.example.backenddemo.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Table(name = "app_user")
public class User extends Author {
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private Boolean isPremium;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;
}
