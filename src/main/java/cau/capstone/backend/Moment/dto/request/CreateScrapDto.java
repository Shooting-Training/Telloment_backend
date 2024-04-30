package cau.capstone.backend.Moment.dto.request;
import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateScrapDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long momentId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    public static CreateScrapDto of(Long momentId, Long userId, String title) {
        return new CreateScrapDto(momentId, userId, title);
    }

}
