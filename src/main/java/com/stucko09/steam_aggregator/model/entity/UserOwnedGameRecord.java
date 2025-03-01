package com.stucko09.steam_aggregator.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOwnedGameRecord extends BaseRecordClass {
    @ManyToOne(optional = false)
    private AppUser appUser;

    @ManyToOne(optional = false)
    private GameRecord gameRecord;
}
