package cau.capstone.backend.model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto extends UserDto{
    private Long userId;

    private String email;
    private String name;
    private String password;
    private String role;

}
