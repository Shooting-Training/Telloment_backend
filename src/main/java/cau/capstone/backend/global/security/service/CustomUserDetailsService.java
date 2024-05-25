package cau.capstone.backend.global.security.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//        return userRepository.findById(Long.parseLong(id))
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND.getMessage()));
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND.getMessage()));
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthorities().toString());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
