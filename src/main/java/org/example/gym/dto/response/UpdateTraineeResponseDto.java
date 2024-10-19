package org.example.gym.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeResponseDto {

    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String address;
    private Set<TrainerResponseDto> trainerList;
}
