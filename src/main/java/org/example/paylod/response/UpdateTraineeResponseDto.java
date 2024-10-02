package org.example.paylod.response;

import java.util.List;
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
    private String dateOfBirth;
    private String address;
    private List<TrainerResponseDto> trainerList;
}
