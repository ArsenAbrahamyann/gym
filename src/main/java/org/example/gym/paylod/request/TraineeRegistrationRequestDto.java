package org.example.gym.paylod.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequestDto {

    @NotBlank
    private String firsName;
    @NotBlank
    private String lastName;
    private String dateOfBrith;
    private String address;

}
