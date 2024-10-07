package org.example.gym.paylod.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String dateOfBirth;
    private String address;
    @NotBlank
    @JsonProperty(value = "isPublic")
    private boolean isPublic;
}
