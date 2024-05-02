package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.User.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
    private String email;

    public static ResponseUserDto of(User user) {
        return new ResponseUserDto(user.getEmail());
    }
}