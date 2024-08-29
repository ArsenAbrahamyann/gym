package org.exemple.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trainer implements Serializable {
    private static final long serialVersionUID = 258L;
    private String userId;
    private String specialization;


}
