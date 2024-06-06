package cau.capstone.backend.page.dto.response;


import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Set;


//책이름, 페이지의 전체 하트의 합, 전체 페이지의 수, 유저 이름 제공
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookDto {

    private Long bookId;
    private Long userId;
    private String userNickname;
    private String bookName;
    private String categoryCode;
    private int totalLikeCount;
    private int totalViewCount;
    private int totalPageCount;
    private List<Long> pageIds;
    @Nullable
    private Set<String> hashtags;

//    public static ResponseBookDto of(Long bookId, String bookName, String categoryCode) {
//        return ResponseBookDto.builder()
//                .bookId(bookId)
//                .bookName(bookName)
//                .categoryCode(categoryCode)
//                .build();
//    }

    public static ResponseBookDto from(Book book) {
        return ResponseBookDto.builder()
                .bookId(book.getId())
                .userId(book.getUser().getId())
                .userNickname(book.getUser().getNickname())
                .bookName(book.getBookName())
                .categoryCode(book.getCategory().getCode())
                .totalViewCount(book.getBookViewCount())
                .totalPageCount(book.getPages().size())
                .pageIds(book.getPageIds())
                .hashtags(book.getHashtagsString())
                .build();
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setTotalLikeCount(int totalLikeCount) {
        this.totalLikeCount = totalLikeCount;
    }
}
