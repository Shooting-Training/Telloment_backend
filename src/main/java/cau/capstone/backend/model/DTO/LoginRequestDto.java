package cau.capstone.backend.model.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "로그인 요청 DTO")
public class LoginRequestDto {

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

}
