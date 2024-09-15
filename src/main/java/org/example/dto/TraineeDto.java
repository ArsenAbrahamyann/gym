package org.example.dto;

import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private Date dateOfBirth;
    private String address;
    private UserDto user;
    private Set<TrainerDto> trainers;
}
