package com.ryanstuckey0.stat_saga_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Playtime extends BaseRecordClass {

    @ManyToOne(optional = false)
    private Game game;

    @ManyToOne(optional = false)
    private AppUser appUser;

    private int playtimeForever;

    @Column(name = "playtime_2weeks")
    private int playtime2Weeks;

    private int playtimeLinuxForever;
    private int playtimeMacForever;
    private int playtimeWindowsForever;
    private int playtimeDeckForever;

    private int diffPlaytimeForever;

    @Column(name = "diff_playtime_2weeks")
    private int diffPlaytime2Weeks;

    private int diffPlaytimeLinux;
    private int diffPlaytimeMac;
    private int diffPlaytimeWindows;
    private int diffPlaytimeDeck;

    private boolean firstUserEntry = false;
    private boolean firstGameEntry = false;
}
