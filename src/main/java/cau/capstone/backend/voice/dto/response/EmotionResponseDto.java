package cau.capstone.backend.voice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmotionResponseDto {

    private String emotion;
    private int value;


    public static EmotionResponseDto of(String emotion, int value) {
        return EmotionResponseDto.builder()
                .emotion(emotion)
                .value(value)
                .build();
    }
}
