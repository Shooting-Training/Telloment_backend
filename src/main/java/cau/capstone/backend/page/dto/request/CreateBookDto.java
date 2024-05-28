package cau.capstone.backend.page.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {


    private String bookName;
    private String categoryCode;

    private Set<String> hashtags;


    public static CreateBookDto createBookDto(String bookName, String categoryCode, Set<String> hashtags){
        return new CreateBookDto(bookName, categoryCode, hashtags);
    }
}
