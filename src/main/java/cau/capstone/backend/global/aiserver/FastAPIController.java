package cau.capstone.backend.global.aiserver;

import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(tags = "For FastAPI")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voice") // '/api/fastapi'로 매핑
public class FastAPIController {

    private final FastAPIService fastApiService;
    private final JwtTokenProvider jwtTokenProvider;

//    @
//    public FastAPIController(FastAPIService fastApiService) {
//        this.fastApiService = fastApiService;
//    }

    @PostMapping("/emotion") // POST 메서드 사용
    public Mono<EmotionDto> getEmotion(@RequestBody String content) {
        // FastApiService에서 비동기적으로 데이터를 가져온다.
        return fastApiService.getEmotionData(content);
    }

    @PostMapping("/speech")
    public Mono<ResponseEntity<byte[]>> speechToText(@RequestHeader String accessToken, @RequestBody String content) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);

        return fastApiService.processStringAndGetWav(userId,content)
                .map(wavFile -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.wav\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(wavFile))
                .defaultIfEmpty(ResponseEntity.status(500).build());
    }

    @PostMapping(value = "/clone", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> cloneVoiceHandler(@RequestPart("user_id") Long userId, @RequestPart("audio_file") FilePart file) {
        return fastApiService.cloneVoice(userId, file);
    }
}