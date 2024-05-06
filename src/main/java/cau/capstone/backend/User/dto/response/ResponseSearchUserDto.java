package cau.capstone.backend.User.dto.response;

import cau.capstone.backend.User.model.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
@Builder
@JsonSerialize
@JsonDeserialize
public class ResponseSearchUserDto implements Serializable {

    private Long userId;
    private String name;
    private String nickName;
    private String image;
    private boolean isFollow;

    public static ResponseSearchUserDto of(User user){
        return ResponseSearchUserDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .name(user.getNickname())
                .image(user.getImage())
                .build();
    }
}