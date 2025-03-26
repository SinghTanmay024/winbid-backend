package com.winbid.backend.model;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class JwtResponse {
    private String jwt;

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }

    // Add other fields if needed (user details, etc.)
}

