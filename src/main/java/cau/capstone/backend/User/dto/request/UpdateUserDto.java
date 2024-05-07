package cau.capstone.backend.User.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {


    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String name;
    private String nickname;
    @Nullable
    private String image;




    public static UpdateUserDto of(String name, String nickname, String image) {
        return new UpdateUserDto(name, nickname, image);
    }

}

