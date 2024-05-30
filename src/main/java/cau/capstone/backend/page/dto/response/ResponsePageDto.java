package cau.capstone.backend.page.dto.response;


import cau.capstone.backend.page.model.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageDto {

    private Long pageId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;

    private Long prevId;
    private Long nextId;
    private Long rootId;

    @Nullable
    private String emotionType;
    private int emotionIntensity;

    private String defaultVoiceUserMail;


//    public static ResponsePageDto from(Page page, int likeCount){
//        return ResponsePageDto.builder()
//                .pageId(page.getId())
//                .userId(page.getUser().getId())
//                .title(page.getTitle())
//                .content(page.getContent())
//                .createdAt(page.getCreatedAt())
//                .emotionType(page.getEmotion().getType().getDescription())
//                .emotionIntensity(page.getEmotion().getIntensity().getIntensity())
//                .voiceInfo(page.getVoiceInfo())
//                .likeCount(likeCount)
//                .build();
//    }


    public static ResponsePageDto from(Page page) {
        return ResponsePageDto.builder()
                .pageId(page.getId())
                .userId(page.getUser().getId())
                .title(page.getTitle())
                .content(page.getContent())
                .createdAt(page.getCreatedAt())
                .emotionType(page.getEmotion().getType().getDescription())
                .emotionIntensity(page.getEmotion().getIntensity().getIntensity())
                .defaultVoiceUserMail(page.getDefaultVoiceUserMail())
                .rootId(page.getRootId())
                .prevId(page.getPrevId())
                .nextId(page.getNextId())
                .build();
    }


    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
