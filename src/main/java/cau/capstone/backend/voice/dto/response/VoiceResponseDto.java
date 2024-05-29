package cau.capstone.backend.voice.dto.response;


import cau.capstone.backend.voice.model.VoiceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoiceResponseDto {
    private long id;
    private String status;
    //nickname, email

    public static VoiceResponseDto of(long id, String status) {
        return VoiceResponseDto.builder()
                .id(id)
                .status(status)
                .build();
    }

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
                .status(status)
                .build();
    }


}
