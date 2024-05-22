package cau.capstone.backend.global;

import cau.capstone.backend.global.aiserver.FastAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final FastAPIService fastAPIService;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/test/emotion")
    public String testEmotion() {
        return fastAPIService.getEmotionData("안녕하세요").block().toString();
    }
}