package org.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {
    private String localDateTime;
    private String address;
    private String userId;
}
