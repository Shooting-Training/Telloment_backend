package cau.capstone.backend.page.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScrapDto {
    @NotNull(message = MessageUtil.NOT_NULL)
    private Long scrapId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userID;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;

    public static UpdateScrapDto of(Long scrapId, Long userID, String title, String content) {
        return new UpdateScrapDto(scrapId, userID, title, content);
    }


}
