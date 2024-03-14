package dev.n7meless.service;

import dev.n7meless.entity.Album;
import dev.n7meless.entity.Post;
import dev.n7meless.entity.User;
import dev.n7meless.entity.enums.ImageType;
import dev.n7meless.exception.UserNotFoundError;
import dev.n7meless.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AlbumService albumService;
    private final UserService userService;

    @Transactional(rollbackFor = IOException.class)
    public Post create(Long userId, Post post, MultipartFile... multipartFiles) {
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundError::new);
        Album album = albumService.saveAlbum(user, ImageType.POST, multipartFiles);
        post.setAlbum(album);
        post.setUser(user);
        return postRepository.save(post);
    }

    public void delete(Long id) {
//        Post post = postRepository.findById(id).orElseThrow();
//        Album album = post.getAlbum();
//        albumService.delete(album);
        Post post = postRepository.findById(id).orElseThrow();
        Album album = post.getAlbum();
        albumService.delete(album);
        postRepository.delete(post);
    }

    public List<Post> getPostsByUserId(Long id) {
        List<Post> posts = postRepository.getPostsByUserIdOrderByCreatedDt(id);
        for (Post post : posts) {
            Album album = post.getAlbum();
            albumService.getImagesFromAlbum(album);
        }
        return posts;
    }
}
