package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    @Serial
    private static final long serialVersionUID = -4045911923399600304L;
    private String firstName;
    private String lastName;
    private String UserName;
    private String password;
    private boolean isActive;

}
