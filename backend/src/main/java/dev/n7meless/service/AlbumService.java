package dev.n7meless.service;

import dev.n7meless.entity.Album;
import dev.n7meless.entity.Image;
import dev.n7meless.entity.User;
import dev.n7meless.entity.enums.ImageType;
import dev.n7meless.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {
    private final ImageService imageService;
    private final AlbumRepository albumRepository;

    public Album saveAlbum(User user, ImageType imageType, MultipartFile... multipartFiles) {
        List<Image> images = imageService.uploadFile(user.getId(), imageType, multipartFiles);
        Album album = new Album();
        album.setUser(user);
        album.setImages(images);
        log.info("album with user id {} saved", user.getId());
        return albumRepository.save(album);
    }

    public void delete(Album album) {
        List<Image> images = album.getImages();
        imageService.deleteFile(images);
    }

    public void getImagesFromAlbum(Album album) {
        List<Image> images = album.getImages();
        imageService.downloadFiles(images);
    }
}
