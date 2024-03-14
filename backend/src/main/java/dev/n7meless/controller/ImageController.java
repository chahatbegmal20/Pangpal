package dev.n7meless.controller;

import dev.n7meless.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final AlbumService albumService;

    public List<byte[]> getImages() {
        return null;
    }
}
