package cau.capstone.backend.Moment.dto.response;


import cau.capstone.backend.Moment.model.Moment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMomentDto {

    private Long momentId;
    private Long userId;
    private String title;
    private String content;
    private boolean isScrapped;

    public static ResponseMomentDto of(Long momentId, Long userId, String title, String content, boolean isScrapped) {
        return ResponseMomentDto.builder()
                .momentId(momentId)
                .userId(userId)
                .title(title)
                .content(content)
                .isScrapped(isScrapped)
                .build();
    }

    public static ResponseMomentDto from(Moment moment) {
        return ResponseMomentDto.builder()
                .momentId(moment.getId())
                .userId(moment.getUser().getId())
                .title(moment.getTitle())
                .content(moment.getContent())
                .isScrapped(moment.isScrapped())
                .build();
    }

}
