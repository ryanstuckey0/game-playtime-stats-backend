package com.stucko09.steam_aggregator.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GamePlaytimeRecord extends BaseRecordClass {

    @ManyToOne(optional = false)
    private GameRecord gameRecord;

    @ManyToOne(optional = false)
    private AppUser appUser;

    private int playtimeForever;
    private int playtime2Weeks;
    private int playtimeLinuxForever;
    private int playtimeMacForever;
    private int playtimeWindowsForever;
    private int playtimeDeckForever;

    private int diffPlaytimeForever;
    private int diffPlaytime2Weeks;
    private int diffPlaytimeLinux;
    private int diffPlaytimeMac;
    private int diffPlaytimeWindows;
    private int diffPlaytimeDeck;

    private boolean isFirstGameEntry = false;
    private boolean isFirstUserEntry = false;
}
