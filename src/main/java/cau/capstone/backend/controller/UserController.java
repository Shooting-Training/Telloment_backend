package cau.capstone.backend.controller;

import cau.capstone.backend.model.request.SearchUserDto;
import cau.capstone.backend.model.request.UpdateUserDto;
import cau.capstone.backend.model.response.ResponseSearchUserDto;
import cau.capstone.backend.model.response.ResponseSimpleUserDto;
import cau.capstone.backend.model.response.ResponseUserDto;
import cau.capstone.backend.security.JwtTokenProvider;
import cau.capstone.backend.service.UserService;
import cau.capstone.backend.util.api.ApiResponse;
import cau.capstone.backend.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 기본정보 조회
    @Operation(summary = "[프로필] 회원 기본정보 조회", description = "회원 기본정보를 조회합니다.")
    @GetMapping("/info/simple")
    public ApiResponse<ResponseSimpleUserDto> getSimpleUserInfo(@RequestHeader String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        return ApiResponse.success(userService.getSimpleUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 조회
    @Operation(summary = "[프로필] 회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("/info")
    public ApiResponse<ResponseUserDto> getUserInfo(@RequestHeader String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        return ApiResponse.success(userService.getUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 수정
    @Operation(summary = "[프로필] 회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/update")
    public ApiResponse<Void> updateUserInfo(@RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUserInfo(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }


    // 회원의 친구 검색 결과 조회
    @Operation(summary = "[주간 랭킹] 회원의 친구 검색 결과 조회", description = "회원의 친구 검색 결과를 조회합니다.")
    @PostMapping("/search")
    public ApiResponse<List<ResponseSearchUserDto>> searchUser(@RequestBody @Valid SearchUserDto searchUserDto) {
        return ApiResponse.success(userService.searchUser(searchUserDto.getUserId(), searchUserDto.getInputName()), ResponseCode.USER_SEARCH_SUCCESS.getMessage());
    }

    // 회원이 특정 회원 팔로우
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우", description = "회원이 특정 회원을 팔로우합니다.")
    @PostMapping("/follow/{toUserId}")
    public ApiResponse<Void> followUser(@RequestHeader String accessToken, @PathVariable Long toUserId) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        userService.followUser(userId, toUserId);
        return ApiResponse.success(null, ResponseCode.USER_FOLLOW_SUCCESS.getMessage());
    }

    // 회원이 특정 회원 팔로우 취소
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우 취소", description = "회원이 특정 회원을 팔로우 취소합니다.")
    @DeleteMapping("/follow/{toUserId}")
    public ApiResponse<Void> unfollowUser(@RequestHeader String accessToken, @PathVariable Long toUserId) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        userService.unfollowUser(userId, toUserId);
        return ApiResponse.success(null, ResponseCode.USER_UNFOLLOW_SUCCESS.getMessage());
    }
}
