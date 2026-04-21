package com.example.backenddemo.post.api;

import com.example.backenddemo.comment.api.dto.CommentRequestDto;
import com.example.backenddemo.comment.api.dto.CommentResponseDto;
import com.example.backenddemo.comment.application.CommentService;
import com.example.backenddemo.post.api.dto.PostRequestDto;
import com.example.backenddemo.post.api.dto.PostResponseDto;
import com.example.backenddemo.post.application.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<PostResponseDto> create(
            @Valid
            @RequestBody
            PostRequestDto post
    ) {
        var savedPost = postService.create(post);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPost);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable
            UUID postId,
            @Valid
            @RequestBody
            CommentRequestDto comment
    ) {
        var savedComment = commentService.create(postId, comment);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedComment);
    }

    // As spring security is not implemented so we can't get authentication and check who liked the post so userId is fetched explicitly by request parameter.
    @PostMapping("/{postId}/likes")
    public ResponseEntity<PostResponseDto> likePost(
            @RequestParam
            UUID authorId,
            @PathVariable
            UUID postId
    ) {
        var post = postService.likePost(authorId, postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(post);
    }
}
