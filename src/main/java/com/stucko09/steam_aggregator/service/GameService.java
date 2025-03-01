package com.stucko09.steam_aggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.model.entity.GamePlaytimeRecord;
import com.stucko09.steam_aggregator.model.entity.GameRecord;
import com.stucko09.steam_aggregator.model.steam.SteamGamePlaytimeRecord;
import com.stucko09.steam_aggregator.repository.GamePlaytimeRecordRepository;
import com.stucko09.steam_aggregator.repository.GameRecordRepository;

@Service
public class GameService {
    @Autowired
    private GameRecordRepository gameRecordRepository;

    @Autowired
    private GamePlaytimeRecordRepository gamePlaytimeRecordRepository;

    @Autowired
    private Mapper dozerBeanMapper;

    public GameRecord saveOrRetrieveGameRecord(SteamGamePlaytimeRecord steamGame) {
        return gameRecordRepository.findBySteamAppId(steamGame.getAppid()).orElseGet(() -> {
            GameRecord newGameRecord = dozerBeanMapper.map(steamGame, GameRecord.class);
            return gameRecordRepository.save(newGameRecord);
        });
    }

    public GameRecord saveNewGameRecord(SteamGamePlaytimeRecord steamGame) {
        GameRecord newGameRecord = dozerBeanMapper.map(steamGame, GameRecord.class);
        return gameRecordRepository.save(newGameRecord);
    }

    public GamePlaytimeRecord saveDailyPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord, AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, gameRecord, user, false);
    }

    public GamePlaytimeRecord saveInitialPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord, AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, gameRecord, user, true);
    }

    private GamePlaytimeRecord saveNewPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord, AppUser user, boolean isFirstUserEntry) {
        GamePlaytimeRecord gamePlaytimeRecord = dozerBeanMapper.map(playtimeRecord, GamePlaytimeRecord.class);
        gamePlaytimeRecord.setGameRecord(gameRecord);
        gamePlaytimeRecord.setAppUser(user);

        // first entry flags default to false; only set them if it's the first user entry or game entry
        // if it's first user entry, it's also logically the first game entry for that user
        if(isFirstUserEntry) {
            gamePlaytimeRecord.setFirstUserEntry(true);
            gamePlaytimeRecord.setFirstGameEntry(true);
        } else if (!gamePlaytimeRecordRepository.existsByGameRecordAndAppUser(gameRecord, user)) {
            gamePlaytimeRecord.setFirstGameEntry(true);
        }

        return gamePlaytimeRecordRepository.save(gamePlaytimeRecord);
    }
}
