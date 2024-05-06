package cau.capstone.backend.page.dto.response;


import cau.capstone.backend.page.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
