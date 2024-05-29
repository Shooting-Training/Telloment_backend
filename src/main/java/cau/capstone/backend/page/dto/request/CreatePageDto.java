package cau.capstone.backend.page.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import cau.capstone.backend.page.model.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePageDto {


    @NotNull(message = MessageUtil.NOT_NULL)
    private Long bookId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String title;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String content;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private Emotion emotion;

    private Set<String> hashtags;


    public static CreatePageDto of(Long bookId, String title, String content, Emotion emotion, Set<String> hashtags) {
        return CreatePageDto.builder()
                .bookId(bookId)
                .title(title)
                .content(content)
                .emotion(emotion)
                .hashtags(hashtags)
                .build();
    }

}
