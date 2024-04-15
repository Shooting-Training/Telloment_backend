package cau.capstone.backend.service;


import cau.capstone.backend.model.DTO.CustomUserInfoDto;
import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import cau.capstone.backend.model.entity.CustomUserDetails;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        CustomUserInfoDto dto = mapper.map(user, CustomUserInfoDto.class);

        return new CustomUserDetails(dto);
    }

}
