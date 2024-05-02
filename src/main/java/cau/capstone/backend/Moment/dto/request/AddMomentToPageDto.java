package cau.capstone.backend.Moment.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMomentToPageDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long momentId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;


    public static AddMomentToPageDto of(Long momentId, Long pageId) {
        return new AddMomentToPageDto(momentId, pageId);
    }

}
