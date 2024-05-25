package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.User.model.User;
import cau.capstone.backend.global.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {


    private String name;
    private String nickname;
    private String email;
    private String password;

    public static CreateUserDto of(String name, String nickname, String email, String password) {
        return new CreateUserDto(name, nickname, email, password);
    }



    public  User toUser(PasswordEncoder passwordEncoder){
        return User.builder()
                .email(email)
                .passwd(passwordEncoder.encode(password))
                .role(Authority.USER)
                .build();
    }
}
