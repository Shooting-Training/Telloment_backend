package cau.capstone.backend.page.controller;


import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.dto.request.*;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.dto.response.ResponseScrapDto;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Api(tags = "2. Page")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/page")
public class PageController {

    private final PageService pageService;
    private final JwtTokenProvider jwtTokenProvider;



    //페이지 관련 기본 동작: 저장, 유저의 페이지 정보 반환, 수정, 삭제
    //모먼트 정보 저장
    @Operation(summary = "페이지 정보 저장")
    @PostMapping("/save")
    public ApiResponse<Long> savePage(@RequestBody @Valid CreatePageDto createPageDto, @RequestParam @JsonFormat LocalDateTime createdAt){
        return ApiResponse.success(pageService.createPage(createPageDto), ResponseCode.PAGE_CREATE_SUCCESS.getMessage());
    }

    @Operation(summary = "유저가 작성한 페이지 정보 반환")
    @GetMapping("/list/{userId}")
    public ApiResponse<List<ResponsePageDto>> getPageList(@RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        List<Page> pageList = pageService.getPageList(userId);

        List<ResponsePageDto> responsePageDtoList = pageList.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responsePageDtoList, ResponseCode.PAGE_READ_SUCCESS.getMessage());
    }


    //페이지 정보 수정
    @Operation(summary = "페이지 정보 수정")
    @PutMapping("/update")
    public ApiResponse<Void> updatePage(@RequestBody @Valid UpdatePageDto updatePageDto,
                                          @RequestParam @JsonFormat LocalDateTime updatedAt){

        pageService.updatePage(updatePageDto, updatedAt);
        return ApiResponse.success(null, ResponseCode.PAGE_UPDATE_SUCCESS.getMessage());
    }

//    //모먼트 삭제
//    @Operation(summary = "모먼트 삭제")
//    @DeleteMapping("/{PageId}/delete")
//    public ApiResponse<Void> deletePage(@PathVariable Long PageId, @RequestHeader Long userId,
//                                          @RequestParam @JsonFormat LocalDateTime deletedAt){
//LocalDate date = LocalDate.of(yy, mm, dd);

    //모먼트 삭제
    @Operation(summary = "페이지 삭제")
    @DeleteMapping("/delete/{pageId}")
    public ApiResponse<Void> deletePage(@PathVariable Long pageId, @RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);

            pageService.deletePage(pageId, userId);
            return ApiResponse.success(null, ResponseCode.PAGE_DELETE_SUCCESS.getMessage());
        }



    //페이지 링크
    @Operation(summary = "페이지 링크")
    @PutMapping("/link")
    public ApiResponse<Void> getPageLink(@RequestBody @Valid LinkPageDto linkPageDto){
        pageService.linkPage(linkPageDto);
        return ApiResponse.success(null, ResponseCode.PAGE_LINK_SUCCESS.getMessage());
    }

    //페이지 좋아요
    @Operation(summary = "페이지 좋아요")
    @PostMapping("/like")
    public ApiResponse<Void> likePage(@RequestBody @Valid LikePageDto likePageDto){
        pageService.likePage(likePageDto);
        return ApiResponse.success(null, ResponseCode.PAGE_LIKE_SUCCESS.getMessage());
    }

    //페이지 좋아요 취소

    @Operation(summary = "페이지 좋아요 취소")
    @DeleteMapping("/dislike")
    public ApiResponse<Void> unlikePage(@RequestBody @Valid LikePageDto likePageDto){
        pageService.dislikePage(likePageDto);
        return ApiResponse.success(null, ResponseCode.PAGE_DISLIKE_SUCCESS.getMessage());
    }


    //특정 날짜에 적은 모먼트 정보 반환
    @Operation(summary = "특정 날짜에 적은 모먼트 정보 반환")
    @GetMapping("/{userId}")
    public ApiResponse<List<ResponsePageDto>> getPageListByDate(@PathVariable Long userId,
                                                                @RequestParam int yy,
                                                                @RequestParam int mm,
                                                                @RequestParam int dd){
        LocalDate date = LocalDate.of(yy, mm, dd);
        List<Page> pageList = pageService.getPageListByDate(userId, date);

        List<ResponsePageDto> responsePageDtoList = pageList.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responsePageDtoList, ResponseCode.PAGE_READ_SUCCESS.getMessage());
    }



}
