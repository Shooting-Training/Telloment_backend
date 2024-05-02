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
public class CreateMomentDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;

    public static CreateMomentDto of(Long userId, String title, String content, Long pageId) {
        return CreateMomentDto.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .pageId(pageId)
                .build();
    }


}
