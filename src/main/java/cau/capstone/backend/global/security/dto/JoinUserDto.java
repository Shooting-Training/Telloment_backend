package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.global.Authority;
import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import cau.capstone.backend.User.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserDto {

    private String token;
    private String name;
    private String nickName;
    private String email;
    private String password;


    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    public CreateUserDto toCreateUserDto() {
        return CreateUserDto.of(name, nickName, email, password);
    }
}
