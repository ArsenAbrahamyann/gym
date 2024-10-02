package org.example.paylod.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeListResponseDto {
    private String traineeName;
    private String lastName;
    private String firstName;
}
