package org.example.paylod.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsActiveRequestDto {

    @NotBlank
    private String username;
    @NotBlank
    private boolean isActive;
}
