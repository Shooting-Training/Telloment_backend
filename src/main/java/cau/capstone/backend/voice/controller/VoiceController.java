package cau.capstone.backend.voice.controller;

import cau.capstone.backend.global.aiserver.EmotionDto;
import cau.capstone.backend.global.aiserver.FastAPIService;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.voice.dto.response.EmotionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voice")
public class VoiceController {

    private final FastAPIService fastAPIService;
    private final PageService pageService;

    @PostMapping("/user/{userId}")
    public String cloneVoice(@RequestPart MultipartFile file, @PathVariable Long userId) {
        Mono<String> test = fastAPIService.cloneVoice(userId, file);
        return test.block();
    }

    @GetMapping("/page/{pageId}")
    public String getEmotionFromPage(@PathVariable Long pageId, @RequestHeader String accessToken) {

        var page = pageService.getPage(accessToken, pageId);
        Mono<EmotionDto> dto = fastAPIService.getEmotionData(page.getContent());
        var res = dto.block();
        String emotion = res.getEmotion();
        int value = res.getValue();


        return EmotionResponseDto.of(emotion, value).toString();
    }

}
