package com.stucko09.steam_aggregator.model.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SteamGamePlaytimeRecord {
    private Long appid;
    private String name;
    private int playtimeForever;

    @JsonProperty("playtime_2weeks")
    private int playtime2Weeks;

    private int playtimeWindowsForever;
    private int playtimeMacForever;
    private int playtimeLinuxForever;
    private int playtimeDeckForever;

    private String imgIconUrl;
    private String imgLogoUrl;
}
