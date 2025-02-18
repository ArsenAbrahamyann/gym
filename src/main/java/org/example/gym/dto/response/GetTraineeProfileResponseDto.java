package org.example.gym.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTraineeProfileResponseDto {

    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBride;
    private String address;
    private boolean isActive;
    private Set<TrainerListResponseDto> trainerList;
}
