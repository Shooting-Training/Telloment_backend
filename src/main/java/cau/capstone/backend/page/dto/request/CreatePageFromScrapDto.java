package cau.capstone.backend.page.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePageFromScrapDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long scrapId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long bookId;

    public static CreatePageFromScrapDto of(Long userId, Long scrapId, Long bookId) {
        return new CreatePageFromScrapDto(userId, scrapId, bookId);
    }
}
