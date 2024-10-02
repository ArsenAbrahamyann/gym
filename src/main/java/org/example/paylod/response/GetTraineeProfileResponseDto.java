package org.example.paylod.response;

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
    private String DateOfBride;
    private String address;
    private boolean isActive;
    private Set<TrainerListResponseDto> trainerList;
}
