package org.exemple.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trainer implements Serializable {
    @Serial
    private static final long serialVersionUID = 3500520733655441205L;
    private String userId;
    private String specialization;


}
