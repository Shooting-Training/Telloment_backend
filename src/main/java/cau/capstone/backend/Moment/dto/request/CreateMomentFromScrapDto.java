package cau.capstone.backend.Moment.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMomentFromScrapDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long scrapId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

    public static CreateMomentFromScrapDto of(Long userId, Long scrapId, Long pageId) {
        return new CreateMomentFromScrapDto(userId, scrapId, pageId);
    }
}
