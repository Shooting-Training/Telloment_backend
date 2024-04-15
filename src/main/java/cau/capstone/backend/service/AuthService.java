package cau.capstone.backend.service;

import cau.capstone.backend.model.DTO.CustomUserInfoDto;
import cau.capstone.backend.model.DTO.LoginRequestDto;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.repository.UserRepository;
import cau.capstone.backend.security.entity.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Transactional
    public String login(LoginRequestDto dto){

        String email = dto.getEmail();
        String password = dto.getPassword();
        User user = userRepository.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        //패스워드 일치 여부 확인
        if (!encoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }
        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);

        String accessToken = jwtProvider.createAccessToken(info);
        return accessToken;


    }
}
