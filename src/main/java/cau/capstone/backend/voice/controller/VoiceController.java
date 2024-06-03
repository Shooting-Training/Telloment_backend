package cau.capstone.backend.voice.controller;

import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.VoiceServerException;
import cau.capstone.backend.voice.aiserver.EmotionDto;
import cau.capstone.backend.voice.aiserver.FastAPIService;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.voice.dto.request.SpeechRequestDto;
import cau.capstone.backend.voice.dto.request.SpeechTestRequestDto;
import cau.capstone.backend.voice.dto.response.EmotionResponseDto;
import cau.capstone.backend.voice.dto.response.VoiceResponseDto;
import cau.capstone.backend.voice.service.VoiceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voice")
@Api(tags = "음성 API")
public class VoiceController {

    private final JwtTokenProvider jwtTokenProvider;
    private final FastAPIService fastAPIService;
    private final PageService pageService;
    private final VoiceService voiceService;

    @PostMapping("user/clone")
    public ApiResponse<String> cloneVoice(@RequestPart FilePart file) {
        Mono<String> test = fastAPIService.cloneVoice(file);
        return ApiResponse.success(test.block(), ResponseCode.VOICE_CLONE_SUCCESS.getMessage());
    }

    @GetMapping("/emotion")
    public ApiResponse<EmotionResponseDto> getEmotionFromPage(@RequestParam("content") String content) {
        Mono<EmotionDto> dto = fastAPIService.getEmotionData(content);
        var res = dto.block();
        String emotion = res.getEmotion();
        int value = res.getValue();

        return ApiResponse.success(EmotionResponseDto.of(emotion, value), ResponseCode.VOICE_EMOTION_SUCCESS.getMessage());
    }

    @GetMapping("/page/{pageId}")
    public ApiResponse<EmotionResponseDto> getEmotionFromPage(@PathVariable Long pageId) {

        var page = pageService.getPage(pageId);
        Mono<EmotionDto> dto = fastAPIService.getEmotionData(page.getContent());
        var res = dto.block();
        String emotion = res.getEmotion();
        int value = res.getValue();

        return ApiResponse.success(EmotionResponseDto.of(emotion, value), ResponseCode.VOICE_EMOTION_SUCCESS.getMessage());
    }

    @GetMapping("/{voiceId}/page/{pageId}/speech")
    public ResponseEntity<byte[]> getSpeechFromPage(@PathVariable Long pageId, @RequestParam("emotion") String emotion, @RequestParam("intensity") int intensity, @PathVariable Long voiceId) {
        var page = pageService.getPage(pageId);
        return fastAPIService.processStringAndGetWav(page.getContent(), emotion, intensity, voiceId)
                .map(wavFile -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.wav\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(wavFile))
                .defaultIfEmpty(ResponseEntity.status(500).build())
                .doOnError(throwable -> {throw new VoiceServerException(ResponseCode.VOICE_SERVER_ERROR);})
                .block();
    }

    @GetMapping("/{voiceId}/speech")
    public ResponseEntity<byte[]> getSpeechTest(@RequestParam("emotion") String emotion, @RequestParam("intensity") int intensity,@RequestParam("content") String content, @PathVariable Long voiceId) {
        return fastAPIService.processStringAndGetWav(content,  emotion, intensity, voiceId)
                .map(wavFile -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.wav\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(wavFile))
                .defaultIfEmpty(ResponseEntity.status(500).build())
                .doOnError(throwable -> {throw new VoiceServerException(ResponseCode.VOICE_SERVER_ERROR);})
                .block();
    }

    @PatchMapping("/{voiceId}/scrap")
    public ApiResponse<String> scrapVoice(@PathVariable Long voiceId) {
        var email = jwtTokenProvider.getUserEmail();
        voiceService.scrapVoiceByUser(email, voiceId);
        return ApiResponse.success("Success", ResponseCode.VOICE_SCRAP_SUCCESS.getMessage());
    }

    @DeleteMapping("/{voiceId}/scrap")
    public ApiResponse<String> deleteScrapVoice(@PathVariable Long voiceId) {
        var email = jwtTokenProvider.getUserEmail();
        voiceService.deleteScrapVoiceByUser(email, voiceId);
        return ApiResponse.success("Success", ResponseCode.VOICE_DELETE_SCRAP_SUCCESS.getMessage());
    }

    @GetMapping("/user/scrap/all")
    public ApiResponse<List<VoiceResponseDto>> getAllScrappedVoice() {
        var email = jwtTokenProvider.getUserEmail();
        return ApiResponse.success(voiceService.getScrappedVoiceList(email), ResponseCode.VOICE_LIST_SUCCESS.getMessage());
    }

    @GetMapping("/all")
    public ApiResponse<Page<VoiceResponseDto>> getAllVoice(@RequestParam int page, @RequestParam int size) {
        var pageable = PageRequest.of(page, size);
        return ApiResponse.success(voiceService.getAllByPage(pageable), ResponseCode.VOICE_LIST_SUCCESS.getMessage());
    }
}
