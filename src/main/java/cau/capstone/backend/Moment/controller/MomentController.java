package cau.capstone.backend.Moment.controller;


import cau.capstone.backend.Moment.dto.request.CreateMomentDto;
import cau.capstone.backend.Moment.dto.request.CreateMomentFromScrapDto;
import cau.capstone.backend.Moment.dto.request.CreateScrapDto;
import cau.capstone.backend.Moment.dto.request.UpdateMomentDto;
import cau.capstone.backend.Moment.dto.response.ResponseMomentDto;
import cau.capstone.backend.Moment.dto.response.ResponseScrapDto;
import cau.capstone.backend.Moment.model.Moment;
import cau.capstone.backend.Moment.service.MomentService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import java.util.stream.Collectors;

@Api(tags = "2. moment")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/moment")
public class MomentController {

    private final MomentService momentService;

    //모먼트 정보 저장
    @Operation(summary = "모먼트 정보 저장")
    @PostMapping("/save")
    public ApiResponse<Long> saveMoment(@RequestBody @Valid CreateMomentDto createMomentDto, @RequestParam @JsonFormat LocalDateTime createdAt){
        return ApiResponse.success(momentService.createMoment(createMomentDto), ResponseCode.MOMENT_CREATE_SUCCESS.getMessage());
    }

    //특정 날짜에 적은 모먼트 정보 반환
    @Operation(summary = "특정 날짜에 적은 모먼트 정보 반환")
    @GetMapping("/{userId}")
    public ApiResponse<List<ResponseMomentDto>> getMomentListByDate(@PathVariable Long userId,
                                                                    @RequestParam int yy,
                                                                    @RequestParam int mm,
                                                                    @RequestParam int dd){
        LocalDate date = LocalDate.of(yy, mm, dd);
        List<Moment> momentList = momentService.getMomentListByDate(userId, date);

        List<ResponseMomentDto> responseMomentDtoList = momentList.stream()
                .map(ResponseMomentDto::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responseMomentDtoList, ResponseCode.MOMENT_READ_SUCCESS.getMessage());
    }

    //모먼트 정보 수정
    @Operation(summary = "모먼트 정보 수정")
    @PostMapping("/update")
    public ApiResponse<Void> updateMoment(@RequestBody @Valid UpdateMomentDto updateMomentDto,
                                          @RequestParam @JsonFormat LocalDateTime updatedAt){

        momentService.updateMoment(updateMomentDto, updatedAt);
        return ApiResponse.success(null, ResponseCode.MOMENT_UPDATE_SUCCESS.getMessage());
    }

//    //모먼트 삭제
//    @Operation(summary = "모먼트 삭제")
//    @DeleteMapping("/{momentId}/delete")
//    public ApiResponse<Void> deleteMoment(@PathVariable Long momentId, @RequestHeader Long userId,
//                                          @RequestParam @JsonFormat LocalDateTime deletedAt){
//LocalDate date = LocalDate.of(yy, mm, dd);

    //모먼트 삭제
    @Operation(summary = "모먼트 삭제")
    @DeleteMapping("/{momentId}/delete")
    public ApiResponse<Void> deleteMoment(@PathVariable Long momentId, @RequestHeader Long userId) {

        momentService.deleteMoment(momentId, userId);
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
    public ApiResponse<List<ResponseScrapDto>> getScrapList(@PathVariable Long userId){
        return ApiResponse.success(momentService.getScrapList(userId), ResponseCode.SCRAP_READ_SUCCESS.getMessage());
    }


    //스크랩 모먼트 해제
    @Operation(summary = "스크랩 모먼트 해제")
    @DeleteMapping("/scrap/{scrapId}")
    public ApiResponse<Void> deleteScrap(@PathVariable Long scrapId, @RequestHeader Long userId){
        momentService.deleteScrap(scrapId, userId);
        return ApiResponse.success(null, ResponseCode.SCRAP_DELETE_SUCCESS.getMessage());
    }

    //스크랩으로 모먼트를 생성
    @Operation(summary = "스크랩으로 모먼트를 생성")
    @PostMapping("/scrap/createfrom")
    public ApiResponse<Long> createFromScrap(@RequestBody @Valid CreateMomentFromScrapDto createScrapDto){
        return ApiResponse.success(momentService.createMomentFromScrap(createScrapDto), ResponseCode.MOMENT_CREATE_SUCCESS.getMessage());
    }

}
