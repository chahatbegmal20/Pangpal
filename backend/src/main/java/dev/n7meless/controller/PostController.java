package dev.n7meless.controller;

import dev.n7meless.entity.Post;
import dev.n7meless.entity.User;
import dev.n7meless.service.PostService;
import dev.n7meless.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPostsByUserId(@RequestParam long userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postService.getPostsByUserId(userId));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create(@RequestParam Long userId,
                                    @RequestPart(name = "post") Post post,
                                    @RequestPart(name = "file") MultipartFile... multipartFiles) {
        postService.create(userId, post, multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON).build();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        postService.delete(id);
    }
}
