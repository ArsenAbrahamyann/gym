package org.example.gym.dto.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private final String username;
    private final String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }
}
