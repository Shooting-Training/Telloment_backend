package cau.capstone.backend.global.security.dto;


import cau.capstone.backend.User.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserDto {

    private Long id;
    private String name;
    private String nickname;
    private String email;

    private int bookCount;
    private int totalLikeCount;
    private int pageCount;


    public static ResponseUserDto of(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .pageCount(0)
                .totalLikeCount(0)
                .bookCount(0)
                .build();
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public void setTotalLikeCount(int totalLikeCount) {
        this.totalLikeCount = totalLikeCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

}