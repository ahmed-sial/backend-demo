package com.example.backenddemo.bot.domain;

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
public class Bot extends Author {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String personaDescription;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    List<Post> posts;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    List<Comment> comments;
}
