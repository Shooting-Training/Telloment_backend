package cau.capstone.backend.model.request;

import cau.capstone.backend.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMomentFromScrapDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long scrapId;

    public static CreateMomentFromScrapDto of(Long userId, Long scrapId) {
        return new CreateMomentFromScrapDto(userId, scrapId);
    }
}
