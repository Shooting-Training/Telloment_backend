package cau.capstone.backend.User.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSimpleUserDto implements Serializable {

    private String name;
    private String image;
}