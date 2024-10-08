package org.example.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerListResponseDto {
    private String trainerName;
    private String firstName;
    private String lastName;
}
