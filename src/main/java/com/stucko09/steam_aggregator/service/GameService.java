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

    public GamePlaytimeRecord saveDailyPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord,
            AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, gameRecord, user, false);
    }

    public GamePlaytimeRecord saveInitialPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord,
            AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, gameRecord, user, true);
    }

    public GamePlaytimeRecord getLastPlaytimeRecord(AppUser appUser, GameRecord gameRecord) {
        return gamePlaytimeRecordRepository.findFirstByGameRecordAndAppUserOrderByCreationTimestampDesc(
                gameRecord,
                appUser);
    }

    /**
     * Saves a new playtime record for Steam and sets flags based on if it's the
     * first user/game entry. If the playtime did not increase from the last record,
     * then no data is saved to the datbase.
     * 
     * @param playtimeRecord   record of playtime info from Steam
     * @param gameRecord       game to update playtime for
     * @param user             user to update playtime for
     * @param isFirstUserEntry true if this is the first time a user is registering
     * @return new playtime record, or null if there is no additional playtime since
     *         it was last saved
     */
    private GamePlaytimeRecord saveNewPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, GameRecord gameRecord,
            AppUser user, boolean isFirstUserEntry) {
        // TODO: could check isFirstUserEntry via DB query instead, determine which way
        // is better

        GamePlaytimeRecord gamePlaytimeRecord = dozerBeanMapper.map(playtimeRecord, GamePlaytimeRecord.class);
        gamePlaytimeRecord.setGameRecord(gameRecord);
        gamePlaytimeRecord.setAppUser(user);

        // if first user entry, also logically the first game entry for that user
        boolean playtimeIncreased = true;
        if (isFirstUserEntry) {
            gamePlaytimeRecord.setFirstUserEntry(true);
            gamePlaytimeRecord.setFirstGameEntry(true);
        } else if (!gamePlaytimeRecordRepository.existsByGameRecordAndAppUser(gameRecord, user)) {
            gamePlaytimeRecord.setFirstGameEntry(true);
        } else {
            playtimeIncreased = updatePlaytimeDiffsFromPreviousEntry(gamePlaytimeRecord, user, gameRecord);
        }

        return playtimeIncreased ? gamePlaytimeRecordRepository.save(gamePlaytimeRecord) : null;
    }

    /**
     * Updates the diffPlaytime* values in {@code newPlaytimeRecord} with the
     * difference between the new playtime and last recorded playtime.
     * 
     * @param newPlaytimeRecord record with latest playtimes that will be updated
     * @param user              used to find last playtime record for user
     * @param game              user to find last playtime record for game
     * @return {@code true} if new playtime values are > 0, else false
     */
    private boolean updatePlaytimeDiffsFromPreviousEntry(
            GamePlaytimeRecord newPlaytimeRecord,
            AppUser user,
            GameRecord game) {
        GamePlaytimeRecord lastPlaytimeRecord = getLastPlaytimeRecord(user, game);

        int diffPlaytimeForever = newPlaytimeRecord.getPlaytimeForever() - lastPlaytimeRecord.getPlaytimeForever();
        if (diffPlaytimeForever == 0) {
            return false;
        }

        newPlaytimeRecord.setDiffPlaytimeForever(diffPlaytimeForever);
        newPlaytimeRecord
                .setDiffPlaytime2Weeks(
                        newPlaytimeRecord.getPlaytime2Weeks() - lastPlaytimeRecord.getPlaytime2Weeks());
        newPlaytimeRecord
                .setDiffPlaytimeLinux(
                        newPlaytimeRecord.getPlaytimeLinuxForever() - lastPlaytimeRecord.getPlaytimeLinuxForever());
        newPlaytimeRecord
                .setDiffPlaytimeMac(
                        newPlaytimeRecord.getPlaytimeMacForever() - lastPlaytimeRecord.getPlaytimeMacForever());
        newPlaytimeRecord
                .setDiffPlaytimeWindows(
                        newPlaytimeRecord.getPlaytimeWindowsForever() - lastPlaytimeRecord.getPlaytimeWindowsForever());
        newPlaytimeRecord
                .setDiffPlaytimeDeck(
                        newPlaytimeRecord.getPlaytimeDeckForever() - lastPlaytimeRecord.getPlaytimeDeckForever());
        return true;
    }
}
