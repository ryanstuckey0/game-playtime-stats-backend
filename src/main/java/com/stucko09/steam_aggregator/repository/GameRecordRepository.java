package com.stucko09.steam_aggregator.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.steam_aggregator.model.entity.GameRecord;

public interface GameRecordRepository extends CrudRepository<GameRecord, Long> {
    public boolean existsBySteamAppId(Long steamAppId);
    public Optional<GameRecord> findBySteamAppId(Long steamAppId);
}
