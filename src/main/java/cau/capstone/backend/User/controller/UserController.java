package cau.capstone.backend.User.controller;

import cau.capstone.backend.User.dto.request.SearchUserDto;
import cau.capstone.backend.User.dto.request.UpdateUserDto;
import cau.capstone.backend.User.dto.response.ResponseScoreDto;
import cau.capstone.backend.User.dto.response.ResponseSearchUserDto;
import cau.capstone.backend.User.dto.response.ResponseSimpleUserDto;
import cau.capstone.backend.User.service.ScoreService;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.User.service.UserService;
import cau.capstone.backend.global.security.dto.ResponseUserDto;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Api(tags = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ScoreService scoreService;


    // 회원 기본정보 조회
    @GetMapping("/me")
    public ApiResponse<ResponseUserDto> getMyUserInfo() {
        return ApiResponse.success(userService.getMyInfo(),"User Info load success");
    }

    @GetMapping("/{email}")
    public ApiResponse<ResponseUserDto> getUserInfo(@PathVariable String email) {
        return ApiResponse.success(userService.getUserInfo(email), "User Info load success");
    }


    // 회원정보 수정
    @Operation(summary = "[프로필] 회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/update")
    public ApiResponse<ResponseUserDto> updateUserInfo(@RequestBody @Valid UpdateUserDto updateUserDto, @RequestHeader String accessToken) {

        return ApiResponse.success(userService.updateUserInfo(updateUserDto, accessToken), ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    @Operation
    @DeleteMapping("/delete")
    public ApiResponse<Long> deleteUser(@RequestHeader String accessToken) {
        ;
        return ApiResponse.success(userService.deleteUser(accessToken), ResponseCode.USER_DELETE_SUCCESS.getMessage());
    }


    //높은 점수를 갖는 카테고리부터 내림차순으로 전부 반환
    @Operation(summary = "유저의 카테고리 점수 반환", description = "유저의 카테고리 점수를 반환합니다.")
    @GetMapping("/score")
    public ApiResponse<List<ResponseScoreDto>> getUserScore(@RequestHeader String accessToken) {

        return ApiResponse.success(scoreService.getSortedScoresByUserId(accessToken), ResponseCode.USER_SCORE_SUCCESS.getMessage());
    }

//
//    // 회원이 특정 회원 팔로우
//    @Operation(summary = "회원이 특정 회원 팔로우", description = "회원이 특정 회원을 팔로우합니다.")
//    @PostMapping("/follow/{followeeId}")
//    public ApiResponse<Void> followUser(@RequestHeader String accessToken, @PathVariable Long followeeId) {
//        userService.followUser(accessToken, followeeId);
//
//        return ApiResponse.success(null, ResponseCode.USER_FOLLOW_SUCCESS.getMessage());
//    }
//
//    // 회원이 특정 회원 팔로우 취소
//    @Operation(summary = " 회원이 특정 회원 팔로우 취소", description = "회원이 특정 회원을 팔로우 취소합니다.")
//    @DeleteMapping("/unfollow/{followeeId}")
//    public ApiResponse<Void> unfollowUser(@RequestHeader String accessToken, @PathVariable Long followeeId) {
//
//        userService.unfollowUser(accessToken, followeeId);
//        return ApiResponse.success(null, ResponseCode.USER_UNFOLLOW_SUCCESS.getMessage());
//    }
}
