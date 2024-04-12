package cau.capstone.helpclosing.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFileName).toString();
    }

    //로컬 파일 다운로드 할 때에는 UrlResource() 메소드에 "file:" + 로컬 파일 경로를 넣어주면 로컬 파일이 다운로드 되었음
    //S3에 올라간 파일은 위와 같이 amazonS3.getUrl(버킷이름, 파일이름)을 통해 파일 다운로드를 할 수 있음
//    public ResponseEntity<UrlResource> downloadImage(String originalFileName) throws MalformedURLException {
//
//        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFileName).toString());
//
//        String contentDisposition = "attachment; filename=\"" +  originalFileName + "\"";
//
//        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(urlResource);
//    }
}
