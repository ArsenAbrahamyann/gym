package org.example.gym.paylod.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileResponseDto {

    private String username;
    private String firstName;
    private String lastName;
    private Long trainingTypeId;
    private boolean isActive;
    private List<TraineeListResponseDto> trainerResponseDtos;
}
