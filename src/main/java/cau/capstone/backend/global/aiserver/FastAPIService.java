package cau.capstone.backend.global.aiserver;

import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.PageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor

public class FastAPIService {

    private final WebClient webClient;
    private final PageRepository pageRepository;


//    public FastAPIService(WebClient webClient) {
//        this.webClient = webClient;
//    }

    public Mono<EmotionDto> getEmotionData(Long pageId) {

        Page page = pageRepository.findPageById(pageId).orElseThrow(() -> new IllegalArgumentException("해당 페이지가 존재하지 않습니다."));

        String content = page.getContent();

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/emotion")
                        .queryParam("text", content)
                        .build())
                .retrieve()
                .bodyToMono(EmotionDto.class);
    }




    public Mono<EmotionDto> getEmotionData(String content) {

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/emotion")
                        .queryParam("text", content)
                        .build())
                .retrieve()
                .bodyToMono(EmotionDto.class);
    }


    public Mono<String> cloneVoice(Long userId, MultipartFile file) {
//        FileSystemResource resource = new FileSystemResource(filePath);

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("audio_file", file.getResource());

//        return this.webClient.post()
//                .uri((uriBuilder -> uriBuilder
//                        .path("/v1/voice/{user_id}")
//                        .queryParam("user_id", userId)
//                        .b
//                        .build())

        return this.webClient.post()
                .uri("/v1/voice/{user_id}", userId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .onStatus(status -> status.value() == 422, response -> {
                    // 로깅 또는 예외 처리
                    return Mono.error(new RuntimeException("Unprocessable Entity"));
                })
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    // 예외 로깅
                    System.err.println("Error response: " + ex.getResponseBodyAsString());
                });
    }

    public Mono<String> speechVoice(Long userId, String content){

        String filename = "speech_" + userId + ".wav";

        Resource aa = (Resource) this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/void/{user_id}/speech")
                        .queryParam("user_id", userId)
                        .queryParam("text", content)
                        .build())
                .retrieve()
                .bodyToMono(ByteArrayResource.class)
                .map(resource -> {
                    try {
                        // 다운로드된 파일을 시스템에 저장
                        Path path = Paths.get("downloaded_" + filename);
                        Files.write(path, resource.getByteArray());
                        return resource;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to write file", e);
                    }
                });


    }
//
//    public Mono<String> transferResource(Resource resource, String filename) {
//        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//        bodyBuilder.asyncPart("file", resource, DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096))
//                .filename(filename)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM);
//
//        MultiValueMap<String, HttpEntity<?>> multipartBody = bodyBuilder.build();
//
//        return webClient.post()
//                .uri("/target-endpoint") // 목적지 서버의 엔드포인트
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(multipartBody))
//                .retrieve()
//                .bodyToMono(String.class);
//    }


    public Mono<String> createData(Object data) {
        return this.webClient.post()
                .uri("/api/data")
                .body(Mono.just(data), Object.class) // 요청 본문 설정
                .retrieve() // 실제 요청을 전송
                .bodyToMono(String.class); // 응답 본문을 Mono<String>으로 변환
    }
}