package org.exemple.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType implements Serializable {
    @Serial
    private static final long serialVersionUID = 8305361785023997429L;
    private String trainingTypeName;
}
