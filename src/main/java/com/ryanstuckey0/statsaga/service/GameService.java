package com.ryanstuckey0.statsaga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.ryanstuckey0.statsaga.model.entity.AppUser;
import com.ryanstuckey0.statsaga.model.entity.Playtime;
import com.ryanstuckey0.statsaga.model.entity.Game;
import com.ryanstuckey0.statsaga.model.steam.SteamGamePlaytimeRecord;
import com.ryanstuckey0.statsaga.repository.GamePlaytimeRecordRepository;
import com.ryanstuckey0.statsaga.repository.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlaytimeRecordRepository gamePlaytimeRecordRepository;

    @Autowired
    private Mapper dozerBeanMapper;

    public Game saveOrRetrieveGame(SteamGamePlaytimeRecord steamGame) {
        return gameRepository.findBySteamAppId(steamGame.getAppid()).orElseGet(() -> {
            Game newGame = dozerBeanMapper.map(steamGame, Game.class);
            return gameRepository.save(newGame);
        });
    }

    public Game saveNewGame(SteamGamePlaytimeRecord steamGame) {
        Game newGame = dozerBeanMapper.map(steamGame, Game.class);
        return gameRepository.save(newGame);
    }

    public Playtime saveDailyPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, Game game,
            AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, game, user, false);
    }

    public Playtime saveInitialPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, Game game,
            AppUser user) {
        return saveNewPlaytimeRecord(playtimeRecord, game, user, true);
    }

    public Playtime getLastPlaytimeRecord(AppUser appUser, Game game) {
        return gamePlaytimeRecordRepository.findFirstByGameAndAppUserOrderByCreationTimestampDesc(
                game,
                appUser);
    }

    /**
     * Saves a new playtime record for Steam and sets flags based on if it's the
     * first user/game entry. If the playtime did not increase from the last record,
     * then no data is saved to the datbase.
     * 
     * @param playtimeRecord   record of playtime info from Steam
     * @param game             game to update playtime for
     * @param user             user to update playtime for
     * @param isFirstUserEntry true if this is the first time a user is registering
     * @return new playtime record, or null if there is no additional playtime since
     *         it was last saved
     */
    private Playtime saveNewPlaytimeRecord(SteamGamePlaytimeRecord playtimeRecord, Game game,
            AppUser user, boolean isFirstUserEntry) {
        // TODO: could check isFirstUserEntry via DB query instead, determine which way
        // is better

        Playtime gamePlaytimeRecord = dozerBeanMapper.map(playtimeRecord, Playtime.class);
        gamePlaytimeRecord.setGame(game);
        gamePlaytimeRecord.setAppUser(user);

        // if first user entry, also logically the first game entry for that user
        boolean playtimeIncreased = true;
        if (isFirstUserEntry) {
            gamePlaytimeRecord.setFirstUserEntry(true);
            gamePlaytimeRecord.setFirstGameEntry(true);
        } else if (!gamePlaytimeRecordRepository.existsByGameAndAppUser(game, user)) {
            gamePlaytimeRecord.setFirstGameEntry(true);
        } else {
            playtimeIncreased = updatePlaytimeDiffsFromPreviousEntry(gamePlaytimeRecord, user, game);
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
            Playtime newPlaytimeRecord,
            AppUser user,
            Game game) {
        Playtime lastPlaytimeRecord = getLastPlaytimeRecord(user, game);

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
