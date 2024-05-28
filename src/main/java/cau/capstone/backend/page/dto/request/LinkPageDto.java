package cau.capstone.backend.page.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkPageDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long prevPageId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long nextPageId;

    public static LinkPageDto of(Long prevPageId, Long nextPageId) {
        return new LinkPageDto(prevPageId, nextPageId);
    }

}
