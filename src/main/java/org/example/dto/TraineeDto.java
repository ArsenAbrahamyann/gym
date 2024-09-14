package org.example.dto;

import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainerEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private Long id;
    private Date dateOfBirth;
    private String address;
    private UserDto user;
    private Set<TrainerEntity> trainers;
}
