package cau.capstone.backend.voice.dto.response;


import cau.capstone.backend.voice.model.VoiceEntity;
import cau.capstone.backend.voice.model.VoiceScrapEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class VoiceResponseDto {
    @JsonProperty("id")
    private long id;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_nickname")
    private String userNickname;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("status")
    private String status;


    public static VoiceResponseDto from(VoiceEntity entity) {
        String status;
        if (entity.getProcessFlag() == 0) {
            status = "Exist";
        } else if (entity.getProcessFlag() == 1) {
            status = "Processing";
        } else {
            status = "Failed";
        }

        return VoiceResponseDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUser().getEmail())
                .userNickname(entity.getUser().getNickname())
                .userId(entity.getUser().getId())
                .status(status)
                .build();
    }

    public static VoiceResponseDto from(VoiceScrapEntity entity) {
        String status;
        if (entity.getVoice().getProcessFlag() == 0) {
            status = "Exist";
        } else if (entity.getVoice().getProcessFlag() == 1) {
            status = "Processing";
        } else {
            status = "Failed";
        }
        return VoiceResponseDto.builder()
                .id(entity.getId().getVoiceId())
                .userEmail(entity.getVoice().getUser().getEmail())
                .userNickname(entity.getVoice().getUser().getNickname())
                .userId(entity.getVoice().getUser().getId())
                .status(status)
                .build();
    }
}
