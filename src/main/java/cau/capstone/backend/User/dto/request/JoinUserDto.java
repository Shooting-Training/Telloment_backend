package cau.capstone.backend.User.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String token;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String nickName;


    @Range(min = 5, max = 100, message = MessageUtil.AGE_RANGE)
    private int age;
}
