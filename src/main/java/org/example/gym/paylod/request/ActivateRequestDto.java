package org.example.gym.paylod.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequestDto {
    private String username;
    @NotBlank
    @JsonProperty(value = "isPublic")
    private boolean isPublic;
}
