package cau.capstone.backend.security.service;

import cau.capstone.backend.model.DTO.UserDto;
import cau.capstone.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityUserServiceImpl implements SecurityUserService{

    private final UserRepository userRepository;

    // login하면 Optional<UserDto>를 반환한다.
    @Override
    public Optional<UserDto> login(UserDto userDto) {
        return userRepository.findUserByLoginId(userDto.loginId())
                .map(UserDto::fromEntity);
    }

}