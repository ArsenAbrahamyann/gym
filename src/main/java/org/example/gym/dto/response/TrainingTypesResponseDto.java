package org.example.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypesResponseDto {
    private String trainingType;
    private Long trainingTypeId;
}
