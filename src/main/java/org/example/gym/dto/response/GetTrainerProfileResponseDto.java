package org.example.gym.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerProfileResponseDto {

    private String firstName;
    private String lastName;
    private Long trainingTypeId;
    private boolean isActive;
    private List<TraineeListResponseDto> traineeListResponseDtos;

}
