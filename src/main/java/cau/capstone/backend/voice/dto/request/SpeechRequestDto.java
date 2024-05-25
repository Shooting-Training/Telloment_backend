package cau.capstone.backend.voice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeechRequestDto {
    private String emotion;
    private int value;
}
