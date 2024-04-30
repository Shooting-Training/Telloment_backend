package cau.capstone.backend.image.service;

import cau.capstone.backend.global.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceIml implements ImageService{

    private final ImageService imageService;

    @Override
    public void uploadImage() {
        log.info("uploadImage");
    }

}
