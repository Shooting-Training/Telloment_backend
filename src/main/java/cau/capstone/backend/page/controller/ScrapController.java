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
@RequestMapping("/api/scrap")
public class ScrapController {

    private final ScrapService scrapService;


    //스크랩 관련 기본 동작: 스크랩, 스크랩 한 페이지 리스트 반환, 스크랩 해제, 스크랩 하면서 페이지 생성
    //페이지 스크랩
    @Operation(summary = "페이지 스크랩")
    @PostMapping("/create")
    public ApiResponse<Long> saveScrap(@RequestBody @Valid CreateScrapDto createScrapDto, @RequestHeader String accessToken){
        return ApiResponse.success(scrapService.saveScrap(createScrapDto, accessToken), ResponseCode.SCRAP_CREATE_SUCCESS.getMessage());
    }


    //스크랩 된 페이지 반환
    @Operation(summary = "스크랩 한 페이지 반환")
    @GetMapping("/scraplist")
    public ApiResponse<List<ResponseScrapDto>> getScrapList(@RequestHeader String accessToken){
        return ApiResponse.success(scrapService.getScrapList(accessToken), ResponseCode.SCRAP_READ_SUCCESS.getMessage());
    }


    //스크랩 페이지 해제
    @Operation(summary = "스크랩 페이지 해제")
    @DeleteMapping("/unscrap/{scrapId}")
    public ApiResponse<Long> deleteScrap(@PathVariable Long scrapId, @RequestHeader String accessToken){

        return ApiResponse.success(scrapService.deleteScrap(scrapId, accessToken), ResponseCode.SCRAP_DELETE_SUCCESS.getMessage());
    }

    //스크랩으로 페이지를 생성
    @Operation(summary = "스크랩으로 페이지를 생성")
    @PostMapping("/createPage")
    public ApiResponse<Long> createFromScrap(@RequestBody @Valid CreatePageFromScrapDto createScrapDto, @RequestHeader String accessToken){
        return ApiResponse.success(scrapService.createPageFromScrap(createScrapDto, accessToken), ResponseCode.PAGE_CREATE_SUCCESS.getMessage());
    }



}
