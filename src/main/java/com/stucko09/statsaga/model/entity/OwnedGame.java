package com.stucko09.statsaga.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserOwnedGameRecord extends BaseRecordClass {
    @ManyToOne(optional = false)
    private AppUser appUser;

    @ManyToOne(optional = false)
    private GameRecord gameRecord;

    @Setter(AccessLevel.NONE)
    private LocalDate dateFirstPlayed;

    private LocalDate dateLastPlayed;

    @Column(nullable = false, updatable = false)
    private LocalDate dateFirstSeen;

    @Setter(AccessLevel.NONE)
    private int playtime;

    public UserOwnedGameRecord(AppUser appUser, GameRecord gameRecord) {
        this.appUser = appUser;
        this.gameRecord = gameRecord;
        this.dateFirstSeen = LocalDate.now();
        playtime = 0;
    }

    /**
     * Sets {@code this.playtime} to the given value and updates
     * {@code this.dateFirstPlayed} and {@code this.dateLastPlayed}
     * only if playtime is greater than 0.
     * 
     * @param totalPlaytime
     */
    public void setPlaytime(int totalPlaytime) {
        if (totalPlaytime > 0) {
            if (playtime == 0) {
                dateFirstPlayed = LocalDate.now();
            }
            playtime = totalPlaytime;
            dateLastPlayed = LocalDate.now();
        }
    }
}
