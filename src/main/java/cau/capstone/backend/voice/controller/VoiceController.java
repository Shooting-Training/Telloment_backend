package cau.capstone.backend.voice.controller;

import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.voice.aiserver.EmotionDto;
import cau.capstone.backend.voice.aiserver.FastAPIService;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.voice.dto.request.SpeechRequestDto;
import cau.capstone.backend.voice.dto.response.EmotionResponseDto;
import cau.capstone.backend.voice.dto.response.VoiceResponseDto;
import cau.capstone.backend.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voice")
public class VoiceController {

    private final JwtTokenProvider jwtTokenProvider;
    private final FastAPIService fastAPIService;
    private final PageService pageService;
    private final VoiceService voiceService;

    @PostMapping("user/{userId}/clone")
    public ApiResponse<String> cloneVoice(@RequestPart FilePart file, @PathVariable Long userId) {
        Mono<String> test = fastAPIService.cloneVoice(userId, file);
        return ApiResponse.success(test.block(), ResponseCode.VOICE_CLONE_SUCCESS.getMessage());
    }

    @GetMapping("/page/{pageId}")
    public ApiResponse<EmotionResponseDto> getEmotionFromPage(@PathVariable Long pageId, @RequestHeader String accessToken) {

        var page = pageService.getPage(accessToken, pageId);
        Mono<EmotionDto> dto = fastAPIService.getEmotionData(page.getContent());
        var res = dto.block();
        String emotion = res.getEmotion();
        int value = res.getValue();

        return ApiResponse.success(EmotionResponseDto.of(emotion, value), ResponseCode.VOICE_EMOTION_SUCCESS.getMessage());
    }

    @GetMapping("/{voiceId}/page/{pageId}/speech")
    public ResponseEntity<byte[]> getSpeechFromPage(@PathVariable Long pageId, @RequestHeader String accessToken, @RequestBody SpeechRequestDto speechRequestDto) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        var page = pageService.getPage(accessToken, pageId);
        return fastAPIService.processStringAndGetWav(userId, page.getContent(), speechRequestDto.getEmotion(), speechRequestDto.getValue())
                .map(wavFile -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.wav\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(wavFile))
                .defaultIfEmpty(ResponseEntity.status(500).build())
                .block();
    }

    @GetMapping("/")
    public List<VoiceResponseDto> getVoiceList() {
        return voiceService.getAccessableVoiceList();
    }

    @PostMapping("/{voiceId}/scrap")
    public ApiResponse<String> scrapVoice(@RequestHeader String accessToken, @PathVariable Long voiceId) {
        voiceService.scrapVoiceByUser(accessToken, voiceId);
        return ApiResponse.success("Success", ResponseCode.VOICE_SCRAP_SUCCESS.getMessage());
    }

    @DeleteMapping("/{voiceId}/scrap")
    public ApiResponse<String> deleteScrapVoice(@RequestHeader String accessToken, @PathVariable Long voiceId) {
        voiceService.deleteScrapVoiceByUser(accessToken, voiceId);
        return ApiResponse.success("Success", ResponseCode.VOICE_DELETE_SCRAP_SUCCESS.getMessage());
    }
}
