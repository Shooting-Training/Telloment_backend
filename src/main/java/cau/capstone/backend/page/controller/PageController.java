package cau.capstone.backend.page.controller;


import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.page.dto.request.*;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.dto.response.ResponseScrapDto;
import cau.capstone.backend.page.model.EmotionType;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Api(tags = "2. Page")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/page")
public class PageController {

    private final PageService pageService;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //페이지 관련 기본 동작: 읽기 ,저장, 유저의 페이지 정보 반환, 수정, 삭제

    //페이지 읽기
    @Operation(summary = "페이지 읽기")
    @GetMapping("/{pageId}")
    public ApiResponse<ResponsePageDto> getPage(@RequestHeader String accessToken, @PathVariable Long pageId){

        return ApiResponse.success(pageService.getPage(accessToken, pageId), ResponseCode.PAGE_READ_SUCCESS.getMessage());
    }

    @Operation(summary = "페이지 전체를 페이저블하게 반환")
    @GetMapping("/allpages")
    public ResponseEntity<org.springframework.data.domain.Page<ResponsePageDto>> getAllPages(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.findAllPages(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "페이지 전체를 감정으로 분류해 페이저블하게 반환")
    @GetMapping("/allpages/emotion")
    public ResponseEntity<org.springframework.data.domain.Page<ResponsePageDto>> getAllPagesByEmotion(
            @RequestParam("emotion") String emotionType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.findPagesByEmotionType(EmotionType.getByCode(emotionType), pageable);
        return ResponseEntity.ok(result);
    }




    //페이지 정보 저장
    @Operation(summary = "페이지 정보 저장")
    @PostMapping("/save")
    public ApiResponse<Long> savePage(@RequestBody @Valid CreatePageDto createPageDto,
                                      @RequestHeader String accessToken){
        return ApiResponse.success(pageService.createPage(createPageDto, accessToken), ResponseCode.PAGE_CREATE_SUCCESS.getMessage());
    }

    @Operation(summary = "유저가 작성한 페이지 정보 반환")
    @GetMapping("/list")
    public ApiResponse<List<ResponsePageDto>> getPageList(@RequestHeader String accessToken){
        Long userId = userRepository.findByEmail(jwtTokenProvider.getUserEmail(accessToken))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND)).getId();

        List<ResponsePageDto> responsePageDtoList = pageService.getPageList(userId);

        return ApiResponse.success(responsePageDtoList, ResponseCode.PAGE_READ_SUCCESS.getMessage());
    }


    //페이지 정보 수정
    @Operation(summary = "페이지 정보 수정")
    @PutMapping("/update")
    public ApiResponse<Long> updatePage(@RequestBody @Valid UpdatePageDto updatePageDto){

        return ApiResponse.success(pageService.updatePage(updatePageDto), ResponseCode.PAGE_UPDATE_SUCCESS.getMessage());
    }

//    //페이지 삭제
//    @Operation(summary = "페이지 삭제")
//    @DeleteMapping("/{PageId}/delete")
//    public ApiResponse<Void> deletePage(@PathVariable Long PageId, @RequestHeader Long userId,
//                                          @RequestParam @JsonFormat LocalDateTime deletedAt){
//LocalDate date = LocalDate.of(yy, mm, dd);

    //페이지 삭제
    @Operation(summary = "페이지 삭제")
    @DeleteMapping("/delete/{pageId}")
    public ApiResponse<Long> deletePage(@PathVariable Long pageId, @RequestHeader String accessToken){
        Long userId = userRepository.findByEmail(jwtTokenProvider.getUserEmail(accessToken))
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND)).getId();


        return ApiResponse.success(pageService.deletePage(pageId, userId), ResponseCode.PAGE_DELETE_SUCCESS.getMessage());
        }



    //페이지 링크
    @Operation(summary = "페이지 링크")
    @PutMapping("/link")
    public ApiResponse<Long> getPageLink(@RequestBody @Valid LinkPageDto linkPageDto){

        return ApiResponse.success(pageService.linkPage(linkPageDto), ResponseCode.PAGE_LINK_SUCCESS.getMessage());
    }

    //페이지 좋아요
    @Operation(summary = "페이지 좋아요")
    @PostMapping("/like")
    public ApiResponse<ResponsePageDto> likePage(@RequestBody @Valid LikePageDto likePageDto, @RequestHeader String accessToken){

        return ApiResponse.success(pageService.likePage(likePageDto, accessToken), ResponseCode.PAGE_LIKE_SUCCESS.getMessage());
    }

    //페이지 좋아요 취소

    @Operation(summary = "페이지 좋아요 취소")
    @DeleteMapping("/dislike")
    public ApiResponse<ResponsePageDto> dislikePage(@RequestBody @Valid LikePageDto likePageDto, @RequestHeader String accessToken){

        return ApiResponse.success(pageService.dislikePage(likePageDto, accessToken), ResponseCode.PAGE_DISLIKE_SUCCESS.getMessage());
    }



    @Operation(summary = "최근 24시간 이내에 생성된 페이지 반환")
    @GetMapping("/recent")
    public ResponseEntity<org.springframework.data.domain.Page<ResponsePageDto>> getPagesCreatedWithinLast24Hours(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.getPagesCreatedWithinLast24Hours(pageable);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "이모션 코드 리스트 반환")
    @GetMapping("/emotionlist")
    public ApiResponse<List<String>> getEmotionList() {
        List<String> emotionList = new ArrayList<>();

        for (EmotionType emotionType : EmotionType.values()) {
            emotionList.add(emotionType.getCode());
        }

        return ApiResponse.success(emotionList, ResponseCode.EMOTION_READ_SUCCESS.getMessage());
    }


}
