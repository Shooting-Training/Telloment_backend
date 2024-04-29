package cau.capstone.backend.security.service;

import cau.capstone.backend.model.request.CreateUserDto;
import cau.capstone.backend.model.request.JoinUserDto;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.repository.UserRepository;
import cau.capstone.backend.security.KakaoUserInfo;
import cau.capstone.backend.security.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {

    private final KakaoUserInfo kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long isSignedUp(String token) { // 클라이언트가 보낸 token을 이용해 카카오 API에 유저 정보를 요청, 유저가 존재하지 않으면 null, 존재하면 회원번호 반환
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        Optional<User> userOptional = userRepository.findByKeyCode(userInfo.getId().toString());
        return userOptional.map(User::getId).orElse(null);
    }

    @Transactional(readOnly = true)
    public CreateUserDto createUserDto(JoinUserDto joinUserDto) { // 카카오로부터 프사 URL, 유저 고유ID를 얻어온 후, 이를 유저가 입력한 정보와 함께 CreateUserDto로 반환
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(joinUserDto.getToken());
        return CreateUserDto.of(joinUserDto.getNickName(), userInfo.getKakao_account().getProfile().getProfile_image_url(),
                userInfo.getId().toString(), joinUserDto.getAge());
    }
}
