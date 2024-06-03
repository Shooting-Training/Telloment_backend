package cau.capstone.backend.global;

import cau.capstone.backend.voice.aiserver.EmotionDto;
import cau.capstone.backend.voice.aiserver.FastAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthCheckController {

    private final FastAPIService fastAPIService;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/test/emotion")
    public String testEmotion() {
        Mono<EmotionDto> dto = fastAPIService.getEmotionData("아 너무 슬프다. 이럴 수가 ");

        String emotion = dto.block().getEmotion();
        System.out.println(emotion);
        double value = dto.block().getValue();
        System.out.println(value);

        return emotion + " " + value;
    }
}