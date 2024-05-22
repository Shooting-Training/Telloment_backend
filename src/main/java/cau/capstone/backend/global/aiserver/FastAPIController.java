package cau.capstone.backend.global.aiserver;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Api(tags = "For FastAPI")
@RestController
public class FastAPIController {

    private final FastAPIService fastApiService;

    @Autowired
    public FastAPIController(FastAPIService fastApiService) {
        this.fastApiService = fastApiService;
    }

    @GetMapping("/api/ai/emotion")
    public Mono<EmotionDto> getEmotion(Long pageId) {
        // FastApiService에서 비동기적으로 데이터를 가져온다.
        return fastApiService.getEmotionData(pageId);
    }

    @PostMapping("/api/create-data")
    public Mono<String> createData(@RequestBody Object data) {
        // FastApiService를 통해 비동기적으로 데이터를 생성한다.
        return fastApiService.createData(data);
    }
}