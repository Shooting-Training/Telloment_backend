package cau.capstone.backend.global.security.controller;

import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.security.dto.*;
import cau.capstone.backend.global.security.service.AuthService;

import cau.capstone.backend.User.service.UserService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "2. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDto> signup(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok(authService.signUp(requestUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody RequestUserDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody RequestTokenDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }



}