package com.stucko09.steam_aggregator.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.model.entity.GameRecord;
import com.stucko09.steam_aggregator.model.entity.UserOwnedGameRecord;

public interface UserOwnedGameRecordRepository extends CrudRepository<UserOwnedGameRecord, Long> {
    public List<UserOwnedGameRecord> findByAppUser(AppUser appUser);
    public boolean existsByAppUserAndGameRecord(AppUser appUser, GameRecord gameRecord);
    public UserOwnedGameRecord findByAppUserAndGameRecord(AppUser appUser, GameRecord gameRecord);
}
