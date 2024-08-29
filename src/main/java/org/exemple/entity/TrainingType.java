package org.exemple.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType implements Serializable {
    private static final long serialVersionUID = 159L;
    private String trainingTypeName;
}
