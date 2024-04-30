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
public class UpdateMomentDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long momentId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;

    private Long rootMomentId;
    private Long prevMomentId;
    private Long nextMomentId;

    public static UpdateMomentDto of(Long momentId, Long userId, String title, String content, Long rootMomentId, Long prevMomentId, Long nextMomentId) {
        return new UpdateMomentDto(momentId, userId, title, content, rootMomentId, prevMomentId, nextMomentId);
    }


}
