package cau.capstone.backend.model.request;


import cau.capstone.backend.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String name;

    @Nullable
    private String image;

    @Range(min = 5, max = 100, message = MessageUtil.AGE_RANGE)
    private int age;

    public static UpdateUserDto of(Long userId, String name, String image, int age) {
        return new UpdateUserDto(userId, name, image,  age);
    }

}

