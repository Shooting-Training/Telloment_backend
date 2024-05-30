package cau.capstone.backend.User.dto.response;


import cau.capstone.backend.User.model.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserLikeDto {

    private Long userId;
    private String userEmail;
    private String name;
    private String nickName;
    private int totalLikes;

    public ResponseUserLikeDto(User user, int totalLikes) {
        this.userId = user.getId();
        this.userEmail = user.getEmail();
        this.name = user.getName();
        this.nickName = user.getNickname();
        this.totalLikes = totalLikes;
    }

//    public static ResponseUserLikeDto from(User user, int totalLikes) {
//        return ResponseUserLikeDto.builder()
//                .user(user)
//                .totalLikes(totalLikes)
//                .build();
//    }

}
