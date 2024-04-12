package cau.capstone.helpclosing.aws;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/s3/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.saveFile(file);
            return ResponseEntity.ok("File uploaded successfully. URL: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

//    @GetMapping("/s3/download")
//    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
//        try {
//            ResponseEntity<UrlResource> response = s3Service.downloadImage(fileName);
//            HttpHeaders headers = response.getHeaders();
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(response.getBody());
//        } catch (MalformedURLException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ByteArrayResource(("Failed to download file: " + e.getMessage()).getBytes()));
//        }
//    }

}
