package cau.capstone.backend.page.dto.response;


import cau.capstone.backend.page.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


//책이름, 페이지의 전체 하트의 합, 전체 페이지의 수, 유저 이름 제공
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookDto {

    private Long bookId;
    private String bookName;

    public static ResponseBookDto of(Long bookId, String bookName) {
        return ResponseBookDto.builder()
                .bookId(bookId)
                .bookName(bookName)
                .build();
    }

    public static ResponseBookDto from(Book book) {
        return ResponseBookDto.builder()
                .bookId(book.getId())
                .bookName(book.getBookName())
                .build();
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }
}
