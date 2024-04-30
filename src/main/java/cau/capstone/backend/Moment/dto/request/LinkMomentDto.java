package cau.capstone.backend.Moment.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkMomentDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long prevMomentId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long nextMomentId;

    public static LinkMomentDto of(Long prevMomentId, Long nextMomentId) {
        return new LinkMomentDto(prevMomentId, nextMomentId);
    }

}
