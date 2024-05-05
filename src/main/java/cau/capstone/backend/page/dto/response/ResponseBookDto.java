package cau.capstone.backend.page.dto.response;


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

    public static ResponseBookDto from(Long bookId, String bookName) {
        return ResponseBookDto.builder()
                .bookId(bookId)
                .bookName(bookName)
                .build();
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }
}
