package cau.capstone.backend.voice.aiserver;

import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class FastAPIService {

    private final WebClient webClient;
    private final PageRepository pageRepository;
    private final JwtTokenProvider jwtTokenProvider;


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


    public Mono<String> cloneVoice(Long userId, FilePart file) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.asyncPart("audio_file", file.content(), DataBuffer.class);

        return this.webClient.post()
                .uri("/v1/voice/{user_id}", userId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .onStatus(status -> status.value() == 422, response -> Mono.error(new RuntimeException("Unprocessable Entity")))
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, ex -> System.err.println("Error response: " + ex.getResponseBodyAsString()));
    }


    public Mono<byte[]> processStringAndGetWav(String token, String content, String emotion, int emotionStrength) {
//        RequestPayload requestPayload = new RequestPayload(id, content);
        var email = jwtTokenProvider.getUserEmail(token);
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/voice/{user_id}/speech")
                        .queryParam("text", content)
                        .queryParam("emotion", emotion)
                        .queryParam("value", emotionStrength)
                        .build(email))
                .retrieve()
                .bodyToMono(byte[].class);
    }


//    public Mono<String> cloneVoice(Long userId, MultipartFile file) {
////        FileSystemResource resource = new FileSystemResource(filePath);
//
//        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//        bodyBuilder.part("audio_file", file.getResource());
//
//        return this.webClient.post()
//                .uri("/v1/voice/{user_id}", userId)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
//                .retrieve()
//                .onStatus(status -> status.value() == 422, response -> {
//                    // 로깅 또는 예외 처리
//                    return Mono.error(new RuntimeException("Unprocessable Entity"));
//                })
//                .bodyToMono(String.class)
//                .doOnError(WebClientResponseException.class, ex -> {
//                    // 예외 로깅
//                    System.err.println("Error response: " + ex.getResponseBodyAsString());
//                });
//    }


}