package org.example.entity;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeEntity {
    private String localDateTime;
    private String address;
    private String userId;
}
