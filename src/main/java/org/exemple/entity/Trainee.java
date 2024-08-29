package org.exemple.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trainee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String localDateTime;
    private String address;
    private String userId;


}
