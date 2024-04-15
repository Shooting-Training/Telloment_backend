package cau.capstone.backend.security.service;

import cau.capstone.backend.model.entity.User;
import cau.capstone.backend.model.repository.UserRepository;
import cau.capstone.backend.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DB에서 UserDetail을 얻어와 AuthenticationManager에게
 * 제공하는 역할
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("이메일 없음");
        }

        return new CustomUserDetails(user);
    }

}
