package cau.capstone.backend.page.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikePageDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    public static LikePageDto of(Long pageId, Long userId) {
        return LikePageDto.builder()
                .pageId(pageId)
                .userId(userId)
                .build();
    }

}
