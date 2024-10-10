package org.example.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequestDto {

    @NotBlank
    private String firsName;
    @NotBlank
    private String lastName;
    private LocalDateTime dateOfBrith;
    private String address;

}
