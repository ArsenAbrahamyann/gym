package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerEntity  {
    private String userId;
    private String specialization;
}
