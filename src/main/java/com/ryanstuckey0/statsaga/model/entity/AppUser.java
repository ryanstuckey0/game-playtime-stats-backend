package com.ryanstuckey0.statsaga.model.entity;

import com.ryanstuckey0.statsaga.model.rest.UserRegistrationRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser extends BaseRecordClass {
    @Column(unique = true)
    private Long steamUserId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Transient
    private String apiKey;

    public AppUser(UserRegistrationRequest userRegistrationRequest) {
        this.username = userRegistrationRequest.getUsername();
        this.email = userRegistrationRequest.getEmail();
        this.steamUserId = userRegistrationRequest.getSteamId();
    }
}
