package cau.capstone.backend.page.controller;


import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.page.dto.request.CreatePageFromScrapDto;
import cau.capstone.backend.page.dto.request.CreateScrapDto;
import cau.capstone.backend.page.dto.response.ResponseScrapDto;
import cau.capstone.backend.page.service.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "3. scrap")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/scrap")
public class ScrapController {

    private final ScrapService scrapService;
    private final JwtTokenProvider jwtTokenProvider;


    //스크랩 관련 기본 동작: 스크랩, 스크랩 한 페이지 리스트 반환, 스크랩 해제, 스크랩 하면서 페이지 생성
    //모먼트 스크랩
    @Operation(summary = "모먼트 스크랩")
    @PostMapping("/scrap")
    public ApiResponse<Long> saveScrap(@RequestBody @Valid CreateScrapDto createScrapDto){
        return ApiResponse.success(scrapService.saveScrap(createScrapDto), ResponseCode.SCRAP_CREATE_SUCCESS.getMessage());
    }


    //스크랩 된 모먼트 반환
    @Operation(summary = "스크랩 한 모먼트 반환")
    @GetMapping("/scraplist/{userId}")
    public ApiResponse<List<ResponseScrapDto>> getScrapList(@PathVariable Long userId){
        return ApiResponse.success(scrapService.getScrapList(userId), ResponseCode.SCRAP_READ_SUCCESS.getMessage());
    }


    //스크랩 모먼트 해제
    @Operation(summary = "스크랩 모먼트 해제")
    @DeleteMapping("/scrap")
    public ApiResponse<Void> deleteScrap(@PathVariable Long scrapId, @RequestHeader Long userId){
        scrapService.deleteScrap(scrapId, userId);
        return ApiResponse.success(null, ResponseCode.SCRAP_DELETE_SUCCESS.getMessage());
    }

    //스크랩으로 모먼트를 생성
    @Operation(summary = "스크랩으로 모먼트를 생성")
    @PostMapping("/scrap/createfrom")
    public ApiResponse<Long> createFromScrap(@RequestBody @Valid CreatePageFromScrapDto createScrapDto){
        return ApiResponse.success(scrapService.createPageFromScrap(createScrapDto), ResponseCode.PAGE_CREATE_SUCCESS.getMessage());
    }



}
