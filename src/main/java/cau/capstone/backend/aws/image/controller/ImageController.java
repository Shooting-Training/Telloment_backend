package cau.capstone.backend.aws.image.controller;

import cau.capstone.backend.global.repository.ImageRepository;
import cau.capstone.backend.aws.image.service.ImageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "Image")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @PostMapping("/upload")
    public void uploadImage() {
    }
}
