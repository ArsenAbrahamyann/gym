package org.example.gym.paylod.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
