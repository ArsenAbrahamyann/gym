package org.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserEntity {
    private String firstName;
    private String lastName;
    private String UserName;
    private String password;
    private boolean isActive;
}
