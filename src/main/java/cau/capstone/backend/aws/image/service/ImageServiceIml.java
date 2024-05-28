package cau.capstone.backend.aws.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceIml implements ImageService{

    @Override
    public void uploadImage() {
        log.info("uploadImage");
    }

}
