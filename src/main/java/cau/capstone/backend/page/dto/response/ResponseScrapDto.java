//package cau.capstone.backend.page.dto.response;
//
//import cau.capstone.backend.page.model.Scrap;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ResponseScrapDto {
//
//    private Long scrapId;
//
//    private String pageTitle;
//    private String pageContent;
//
//
//
//    public static ResponseScrapDto from(Scrap scrap) {
//        return ResponseScrapDto.builder()
//                .scrapId(scrap.getId())
//                .pageTitle(scrap.getPage().getTitle())
//                .pageContent(scrap.getPage().getContent())
//                .build();
//    }
//    public Long getScrapId() {
//        return scrapId;
//    }
//
//
//}
