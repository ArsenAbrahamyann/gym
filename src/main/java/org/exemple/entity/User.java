package org.exemple.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = 426879L;

    private String firstName;
    private String lastName;
    private String UserName;
    private String password;
    private boolean isActive;

}
