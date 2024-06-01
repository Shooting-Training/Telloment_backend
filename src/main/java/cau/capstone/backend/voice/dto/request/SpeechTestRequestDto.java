package cau.capstone.backend.voice.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeechTestRequestDto {
    private String emotion;
    private int value;
    private String content;
}
