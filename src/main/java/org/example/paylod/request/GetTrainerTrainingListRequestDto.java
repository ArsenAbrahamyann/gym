package org.example.paylod.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingListRequestDto {

    @NotBlank
    private String trainerUsername;
    private String periodFrom;
    private String periodTo;
    private String traineeName;
}
