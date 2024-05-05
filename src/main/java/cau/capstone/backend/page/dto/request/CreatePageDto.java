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
public class CreatePageDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long bookId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;

    public static CreatePageDto of(Long userId, String title, String content, Long bookId) {
        return CreatePageDto.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .bookId(bookId)
                .build();
    }


}
