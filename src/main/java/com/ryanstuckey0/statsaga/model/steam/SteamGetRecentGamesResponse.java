package com.ryanstuckey0.statsaga.model.steam;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SteamGetRecentGamesResponse {
    private String totalCount;
    private List<SteamGamePlaytimeRecord> games;
}
