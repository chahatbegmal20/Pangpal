package dev.n7meless.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import dev.n7meless.entity.Album;
import dev.n7meless.entity.Image;
import dev.n7meless.entity.User;
import dev.n7meless.entity.enums.ImageType;
import dev.n7meless.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    @Value("${application.bucket.name}")
    private String bucketName;
    private final AmazonS3 s3Client;
    private static final String IMAGE_PATH = "/images";

    public List<Image> uploadFile(Long userId, ImageType imageType, MultipartFile... multipartFiles) {
        List<Image> images = convertMultipartFilesToList(userId, imageType, multipartFiles);
        for (Image image : images) {
            File file = new File(image.getFileName());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(image.getBytes());

                s3Client.putObject(new PutObjectRequest(bucketName, image.getPath(), file));
            } catch (IOException e) {
                log.error("an occurred error when saving image type {} with user {}",
                        imageType.getType(), userId);
            }
        }
        log.info("images with type {} was upload to s3 storage by user {}",
                imageType.getType(), userId);
        return images;
    }

    public List<Image> convertMultipartFilesToList(Long userId, ImageType imageType, MultipartFile... multipartFiles) {
        final List<Image> images = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {

            if (file.getSize() != 0) {
                try {
                    String path = String.format("%s/%s/%s/%s", IMAGE_PATH, userId, imageType.getType(), UUID.randomUUID());
                    Image image = new Image();
                    image.setBytes(file.getBytes());
                    image.setPath(path);
                    image.setImageType(imageType);
                    image.setSize(file.getSize());
                    image.setContentType(file.getContentType());
                    image.setFileName(file.getOriginalFilename());
                    images.add(image);
                } catch (IOException e) {
                    log.error("An error occurred while convert image {} with content type {} and size {}", file.getOriginalFilename(), file.getContentType(), file.getSize());
                }
            }
        }
        return images;
    }

    public void deleteFile(List<Image> images) {
        Album album = images.get(0).getAlbum();
        User user = album.getUser();
        for (Image image : images) {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, image.getPath()));
        }
        log.info("images in path {} was deleted from s3 storage by user {}",
                images.get(0).getPath(), user.getId());
    }

    //TODO подумать над реализации извлечения фотографий с хранилища
    public List<Image> downloadFiles(List<Image> images) {
        for (Image image : images) {
            try {
                S3Object s3Object = s3Client.getObject(bucketName, image.getPath());
                S3ObjectInputStream inputStream = s3Object.getObjectContent();
                byte[] content = IOUtils.toByteArray(inputStream);
                image.setBytes(content);
            } catch (IOException e) {
                log.error("an occurred error with image {} in path {} when convert s3 object to bytes",
                        image.getPath(), image.getFileName());
            }
        }
        return images;
    }
}
