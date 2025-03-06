package com.stucko09.steam_aggregator.repository;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.model.entity.GamePlaytimeRecord;
import com.stucko09.steam_aggregator.model.entity.GameRecord;

public interface GamePlaytimeRecordRepository extends CrudRepository<GamePlaytimeRecord, Long> {
    public boolean existsByAppUser(AppUser appUser);

    public boolean existsByGameRecordAndAppUser(GameRecord gameRecord, AppUser appUser);

    public GamePlaytimeRecord findFirstByGameRecordAndAppUserOrderByCreationTimestampDesc(GameRecord gameRecord,
            AppUser appUser);
}
