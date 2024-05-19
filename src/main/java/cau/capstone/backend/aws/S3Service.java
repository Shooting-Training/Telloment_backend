package cau.capstone.backend.aws;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }

    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
//
//    @Override
//    public ResponseEntity<?> downloadFileBlob(long id, HttpServletRequest request, HttpServletResponse response) {
//        File file = findById(id);
//
//        String downloadFileName = file.getName();
//
//        try {
//            String fileName = makeFileName(request, Objects.requireNonNullElse(downloadFileName, file.getKey()));
//            S3Object s3Object = amazonS3.getObject(bucket, file.getKey());
//            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
//            headers.setContentDispositionFormData("attachment", fileName);
//
//            // 파일을 스트리밍 방식으로 응답
//            return new ResponseEntity<>(new InputStreamResource(objectInputStream), headers, HttpStatus.OK);
//        } catch (IOException e) {
//            log.debug(e.getMessage(), e);
//            throw new BusinessException(ApiResponseCode.FILE_DOWNLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * 파일명이 한글인 경우 URL encode이 필요함.
     * @param request
     * @param displayFileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String makeFileName(HttpServletRequest request, String displayFileName) throws UnsupportedEncodingException {
        String header = request.getHeader("User-Agent");

        String encodedFilename = null;
        if (header.contains("MSIE")) {
            encodedFilename = URLEncoder.encode(displayFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } else if (header.contains("Trident")) {
            encodedFilename = URLEncoder.encode(displayFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } else if (header.contains("Chrome")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < displayFileName.length(); i++) {
                char c = displayFileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, StandardCharsets.UTF_8));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else if (header.contains("Opera")) {
            encodedFilename = "\"" + new String(displayFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"";
        } else if (header.contains("Safari")) {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"", StandardCharsets.UTF_8);
        } else {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"", StandardCharsets.UTF_8);
        }

        return encodedFilename;

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
