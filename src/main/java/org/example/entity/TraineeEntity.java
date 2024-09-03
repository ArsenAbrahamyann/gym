package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = 83652714433742173L;
    private String localDateTime;
    private String address;
    private String userId;


}
