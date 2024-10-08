package org.example.gym.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequestDto {
    private String username;

    @NotBlank
    @JsonProperty(value = "isPublic")
    private boolean isPublic;
}