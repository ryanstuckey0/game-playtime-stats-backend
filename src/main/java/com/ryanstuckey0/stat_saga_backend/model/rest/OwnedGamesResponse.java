package com.ryanstuckey0.stat_saga_backend.model.rest;

import com.github.dozermapper.core.Mapping;

import lombok.Data;

@Data
public class OwnedGamesResponse {
    @Mapping("game.name")
    private String gameName;

    @Mapping("game.steamAppId")
    private String steamAppId;

    @Mapping("game.id")
    private String gameId;

    @Mapping("appUser.username")
    private String username;

    @Mapping("appUser.id")
    private String userId;
}
