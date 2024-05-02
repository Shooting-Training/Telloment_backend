package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.global.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import cau.capstone.backend.User.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {

    private String email;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder){
        return User.builder()
                .email(email)
                .passwd(passwordEncoder.encode(password))
                .role(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
