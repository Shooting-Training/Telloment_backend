package cau.capstone.backend.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public ErrorResponseDto(int value, String message, LocalDateTime now)
    {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }
}
