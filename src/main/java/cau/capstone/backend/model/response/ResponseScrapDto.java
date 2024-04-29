package cau.capstone.backend.model.response;

import cau.capstone.backend.model.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseScrapDto {
    private Long scrapId;
    private int count;

    public static ResponseScrapDto of(Long scrapId, int count) {
        return ResponseScrapDto.builder()
                .scrapId(scrapId)
                .count(count)
                .build();
    }

    public static ResponseScrapDto from(Scrap scrap) {
        return ResponseScrapDto.builder()
                .scrapId(scrap.getId())
                .count(scrap.getCount())
                .build();
    }


}
