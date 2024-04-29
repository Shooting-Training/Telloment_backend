package cau.capstone.backend.controller;


import cau.capstone.backend.model.request.CreateMomentDto;
import cau.capstone.backend.model.request.CreateMomentFromScrapDto;
import cau.capstone.backend.model.request.CreateScrapDto;
import cau.capstone.backend.model.request.UpdateMomentDto;
import cau.capstone.backend.model.response.ResponseMomentDto;
import cau.capstone.backend.service.MomentService;
import cau.capstone.backend.util.api.ApiResponse;
import cau.capstone.backend.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "2. moment")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/moment")
public class MomentController {

    private final MomentService momentService;

    //모먼트 정보 저장
    @Operation(summary = "모먼트 정보 저장")
    @PostMapping("/save")
    public ApiResponse<Long> saveMoment(@RequestBody @Valid CreateMomentDto createMomentDto){
        return ApiResponse.success(momentService.saveMoment(createMomentDto), ResponseCode.MOMENT_CREATE_SUCCESS.getMessage());
    }

    //특정 날짜에 적은 모먼트 정보 반환
    @Operation(summary = "특정 날짜에 적은 모먼트 정보 반환")
    @GetMapping("/{userId}")
    public ApiResponse<List<ResponseMomentDto>> getMomentListByDate(@PathVariable Long userId,
                                                                    @RequestParam int yy,
                                                                    @RequestParam int mm,
                                                                    @RequestParam int dd){
        LocalDate date = LocalDate.of(yy, mm, dd);
        return ApiResponse.success(momentService.getMomentListByDate(userId, date), ResponseCode.MOMENT_READ_SUCCESS.getMessage());
    }

    //모먼트 정보 수정
    @Operation(summary = "모먼트 정보 수정")
    @PostMapping("/update")
    public ApiResponse<Void> updateMoment(@RequestBody @Valid UpdateMomentDto updateMomentDto,
                                          @RequestParam int yy,
                                          @RequestParam int mm,
                                          @RequestParam int dd){
        LocalDate date = LocalDate.of(yy, mm, dd);
        momentService.updateMoment(updateMomentDto, date);
        return ApiResponse.success(null, ResponseCode.MOMENT_UPDATE_SUCCESS.getMessage());
    }

    //모먼트 삭제
    @Operation(summary = "모먼트 삭제")
    @DeleteMapping("/{momentId}/delete")
    public ApiResponse<Void> deleteMoment(@PathVariable Long momentId, @RequestHeader Long userId,
                                          @RequestParam int yy,
                                          @RequestParam int mm,
                                          @RequestParam int dd){
        LocalDate date = LocalDate.of(yy, mm, dd);
        momentService.deleteMoment(momentId, userId, date);
        return ApiResponse.success(null, ResponseCode.MOMENT_DELETE_SUCCESS.getMessage());
    }

    //모먼트 스크랩
    @Operation(summary = "모먼트 스크랩")
    @PostMapping("/scrap")
    public ApiResponse<Long> saveScrap(@RequestBody @Valid CreateScrapDto createScrapDto){
        return ApiResponse.success(momentService.saveScrap(createScrapDto), ResponseCode.SCRAP_CREATE_SUCCESS.getMessage());
    }


    //스크랩 된 모먼트 반환
    @Operation(summary = "스크랩 된 모먼트 반환")
    @GetMapping("/scrap/{userId}")
    public ApiResponse<List<ResponseMomentDto>> getScrapList(@PathVariable Long userId){
        return ApiResponse.success(momentService.getScrapList(userId), ResponseCode.SCRAP_READ_SUCCESS.getMessage());
    }

    //스크랩 모먼트 수정
    @Operation(summary = "스크랩 모먼트 수정")
    @PostMapping("/scrap/update")
    public ApiResponse<Void> updateScrap(@RequestBody @Valid UpdateMomentDto updateMomentDto){
        momentService.updateScrap(updateMomentDto);
        return ApiResponse.success(null, ResponseCode.SCRAP_UPDATE_SUCCESS.getMessage());
    }

    //스크랩 모먼트 해제
    @Operation(summary = "스크랩 모먼트 해제")
    @DeleteMapping("/scrap/{scrapId}")
    public ApiResponse<Void> deleteScrap(@PathVariable Long scrapId, @RequestHeader Long userId){
        momentService.deleteScrap(momentId);
        return ApiResponse.success(null, ResponseCode.SCRAP_DELETE_SUCCESS.getMessage());
    }

    //스크랩으로 모먼트를 생성
    @Operation(summary = "스크랩으로 모먼트를 생성")
    @PostMapping("/scrap/createfrom")
    public ApiResponse<Long> createFromScrap(@RequestBody @Valid CreateMomentFromScrapDto createScrapDto){
        return ApiResponse.success(momentService.createMomentFromScrap(createScrapDto), ResponseCode.MOMENT_CREATE_SUCCESS.getMessage());
    }

}
