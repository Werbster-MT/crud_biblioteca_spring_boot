package com.example.biblioteca.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessDTO {

    private String token;
    private String role;
    private String username;

    public AccessDTO(String token, String role, String username) {
        this.token = token;
        this.role = role;
        this.username = username;
    }
}
