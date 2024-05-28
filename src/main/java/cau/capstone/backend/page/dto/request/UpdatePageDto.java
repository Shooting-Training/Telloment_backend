package cau.capstone.backend.page.dto.request;

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
public class UpdatePageDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;



    public static UpdatePageDto of(Long pageId, String title, String content) {
        return UpdatePageDto.builder()
                .pageId(pageId)
                .title(title)
                .content(content)
                .build();
    }


}
