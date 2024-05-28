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
public class CreateScrapDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

//    @NotBlank(message = MessageUtil.NOT_BLANK)
//    private String title;

    public static CreateScrapDto of(Long pageId) {
        return new CreateScrapDto(pageId);
    }

}
