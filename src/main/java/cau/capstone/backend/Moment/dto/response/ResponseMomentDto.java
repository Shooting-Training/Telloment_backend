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


    public static ResponseMomentDto of(Moment moment) {
        return ResponseMomentDto.builder()
                .momentId(moment.getId())
                .userId(moment.getUser().getId())
                .title(moment.getTitle())
                .content(moment.getContent())
                .build();
    }

    public static ResponseMomentDto from(Moment moment) {
        return ResponseMomentDto.builder()
                .momentId(moment.getId())
                .userId(moment.getUser().getId())
                .title(moment.getTitle())
                .content(moment.getContent())
                .build();
    }

}
