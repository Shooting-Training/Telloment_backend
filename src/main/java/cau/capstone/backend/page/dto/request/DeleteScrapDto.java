package cau.capstone.backend.page.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteScrapDto {

    private Long scrapId;
    private Long userId;

    public static DeleteScrapDto of(Long scrapId, Long userId) {
        return DeleteScrapDto.builder()
                .scrapId(scrapId)
                .userId(userId)
                .build();
    }

    public Long getScrapId() {
        return scrapId;
    }

    public Long getUserId() {
        return userId;
    }
}
