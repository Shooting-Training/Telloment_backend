package cau.capstone.backend.model.request;

import cau.capstone.backend.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String inputName;

    public static SearchUserDto of(Long userId, String inputName) {
        return new SearchUserDto(userId, inputName);
    }
}