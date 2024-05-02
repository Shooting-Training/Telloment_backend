package cau.capstone.backend.Moment.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMomentDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long momentId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;


    public static UpdateMomentDto of(Long momentId, Long userId, String title, String content) {
        return UpdateMomentDto.builder()
                .momentId(momentId)
                .userId(userId)
                .title(title)
                .content(content)
                .build();
    }


}
