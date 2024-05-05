package cau.capstone.backend.page.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {


    private String bookName;


    public static CreateBookDto of(String bookName) {
        return new CreateBookDto(bookName);
    }
}
