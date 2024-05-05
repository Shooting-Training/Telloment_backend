package cau.capstone.backend.page.dto.request;


import cau.capstone.backend.global.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPageToBookDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long pageId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long bookId;


    public static AddPageToBookDto of(Long pageId, Long bookId) {
        return new AddPageToBookDto(pageId, bookId);
    }

}
