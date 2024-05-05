package cau.capstone.backend.page.dto.response;


import cau.capstone.backend.page.model.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageDto {

    private Long pageId;
    private Long userId;
    private String title;
    private String content;


    public static ResponsePageDto of(Page page) {
        return ResponsePageDto.builder()
                .pageId(page.getId())
                .userId(page.getUser().getId())
                .title(page.getTitle())
                .content(page.getContent())
                .build();
    }

    public static ResponsePageDto from(Page page) {
        return ResponsePageDto.builder()
                .pageId(page.getId())
                .userId(page.getUser().getId())
                .title(page.getTitle())
                .content(page.getContent())
                .build();
    }

}
