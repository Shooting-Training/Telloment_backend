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


    public static LikePageDto of(Long pageId) {
        return LikePageDto.builder()
                .pageId(pageId)
                .build();
    }

}
