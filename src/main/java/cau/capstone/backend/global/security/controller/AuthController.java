package cau.capstone.backend.global.security.controller;

import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.security.dto.*;
import cau.capstone.backend.global.security.service.AuthService;

import cau.capstone.backend.User.service.UserService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "2. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;


    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody JoinUserDto joinUserDto) {

        CreateUserDto createUserDto = joinUserDto.toCreateUserDto();
        userService.saveUser(createUserDto);
//        TokenDto tokenDto = authService.login(joinUserDto);

        return ApiResponse.success("sign up success", ResponseCode.USER_SIGNUP_SUCCESS.getMessage());

    }

    @PostMapping("/login")
    public ApiResponse<TokenDto> login(@RequestBody LoginUserDto loginUserDto) {
        return ApiResponse.success(authService.login(loginUserDto), ResponseCode.USER_LOGIN_SUCCESS.getMessage());
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenDto> reissue(@RequestBody RequestTokenDto tokenRequestDto) {
        return ApiResponse.success(authService.reissue(tokenRequestDto), ResponseCode.TOKEN_REISSUE_SUCCESS.getMessage());
    }

    @GetMapping("/token")
    public ApiResponse<Boolean> tokenCheck(@RequestHeader String accessToken) {
        return ApiResponse.success(jwtTokenProvider.validateToken(accessToken), ResponseCode.TOKEN_CHECK_SUCCESS.getMessage());
    }

    @PatchMapping("/voice_permission")
    public ApiResponse<String> voicePermission(@RequestParam("permit") boolean permit) {
        var email = jwtTokenProvider.getUserEmail();
        authService.updateVoicePermission(email, permit);
        return ApiResponse.success("success", ResponseCode.VOICE_PERMISSION_UPDATE_SUCCESS.getMessage());
    }
}