package cau.capstone.backend.global.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestTokenDto {
    private String accessToken;
    private String refreshToken;
}