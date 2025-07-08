package com.statsaga.backend.model.rest;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String email;
    private Long steamId;
    private String steamApiKey;
}
