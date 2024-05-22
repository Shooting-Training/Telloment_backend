package cau.capstone.backend.global.aiserver;

import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.PageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public Mono<String> createData(Object data) {
        return this.webClient.post()
                .uri("/api/data")
                .body(Mono.just(data), Object.class) // 요청 본문 설정
                .retrieve() // 실제 요청을 전송
                .bodyToMono(String.class); // 응답 본문을 Mono<String>으로 변환
    }
}