package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.User.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserDto {

    private Long id;
    private String name;
    private String nickname;
    private String email;


    public static ResponseUserDto of(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

}