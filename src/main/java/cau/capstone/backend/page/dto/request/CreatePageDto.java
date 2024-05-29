package cau.capstone.backend.page.dto.request;

import cau.capstone.backend.global.util.MessageUtil;
import cau.capstone.backend.page.model.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

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

    private String emotionType;

    private int emotionIntensity;

    private Set<String> hashtags;


    public static CreatePageDto of(Long bookId, String title, String content, String emotionType, int emotionIntensity, Set<String> hashtags) {
        return CreatePageDto.builder()
                .bookId(bookId)
                .title(title)
                .content(content)
                .emotionType(emotionType)
                .emotionIntensity(emotionIntensity)
                .hashtags(hashtags)
                .build();
    }

}
