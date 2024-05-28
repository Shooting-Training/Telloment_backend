package cau.capstone.backend.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTokenDto {
    private String accessToken;
    private String refreshToken;




}