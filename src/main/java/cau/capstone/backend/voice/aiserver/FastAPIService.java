package cau.capstone.backend.voice.aiserver;

import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.PageRepository;
import cau.capstone.backend.voice.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class FastAPIService {


    private final PageRepository pageRepository;
    private final UserRepository userRepository;
    private final VoiceRepository voiceRepository;

    private final WebClient webClient;
    private final JwtTokenProvider jwtTokenProvider;



    public Mono<EmotionDto> getEmotionData(String content) {

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/emotion")
                        .queryParam("text", content)
                        .build())
                .retrieve()
                .bodyToMono(EmotionDto.class);
    }


//    public Mono<String> cloneVoice(FilePart file) {
//        var email = jwtTokenProvider.getUserEmail();
//        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//        bodyBuilder.asyncPart("audio_file", file.content(), DataBuffer.class);
//
//        return this.webClient.post()
//                .uri("/v1/voice/{user_id}", email)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
//                .retrieve()
//                .onStatus(status -> status.value() == 422, response -> Mono.error(new RuntimeException("Unprocessable Entity")))
//                .bodyToMono(String.class)
//                .doOnError(WebClientResponseException.class, ex -> System.err.println("Error response: " + ex.getResponseBodyAsString()));
//    }

    public Mono<String> cloneVoice(MultipartFile file) {
        var email = jwtTokenProvider.getUserEmail();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."))
                .getId();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("audio_file", file.getResource());

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


    public Mono<byte[]> processStringAndGetWav(String content, String emotion, int emotionStrength, Long voiceId) {
        //check voice permission
        var voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new RuntimeException("해당 음성이 존재하지 않습니다."));

        if(!voice.getUser().getVoiceUsePermissionFlag()) {
            throw new RuntimeException("음성 사용 권한이 없습니다.");
        }

        var voiceUserEmail = voice.getUser().getEmail();

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/voice/{user_id}/speech")
                        .queryParam("text", content)
                        .queryParam("emotion", emotion)
                        .queryParam("strength", emotionStrength)
                        .build(voiceUserEmail))
                .retrieve()
                .bodyToMono(byte[].class);
    }

}