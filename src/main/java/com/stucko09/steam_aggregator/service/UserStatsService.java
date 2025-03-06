package com.stucko09.steam_aggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.model.entity.GamePlaytimeRecord;
import com.stucko09.steam_aggregator.model.entity.GameRecord;
import com.stucko09.steam_aggregator.model.entity.UserOwnedGameRecord;
import com.stucko09.steam_aggregator.model.steam.SteamGamePlaytimeRecord;
import com.stucko09.steam_aggregator.model.steam.SteamGetRecentGamesResponse;
import com.stucko09.steam_aggregator.repository.UserOwnedGameRecordRepository;

@Service
public class UserStatsService {

    @Autowired
    private SteamApiService steamApiService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserOwnedGameRecordRepository userOwnedGameRecordRepository;

    public void collectAndSaveInitialPlaytimeStats(AppUser user) {
        SteamGetRecentGamesResponse ownedGamesResponse = steamApiService
                .getOwnedGames(user.getSteamUserId(), user.getApiKey())
                .getResponse();

        registerGamesAndSavePlaytimeStats(user, ownedGamesResponse, true);
    }

    public void collectAndSaveDailyPlaytimeStats(AppUser user) {
        SteamGetRecentGamesResponse recentGamesResponse = steamApiService
                .getRecentlyPlayedGames(user.getSteamUserId(), user.getApiKey())
                .getResponse();

        registerGamesAndSavePlaytimeStats(user, recentGamesResponse, false);
    }

    private void registerGamesAndSavePlaytimeStats(AppUser user, SteamGetRecentGamesResponse recentGamesResponse,
            boolean isInitialPlaytimeStats) {
        for (SteamGamePlaytimeRecord steamGame : recentGamesResponse.getGames()) {
            GameRecord gameRecord = gameService.saveOrRetrieveGameRecord(steamGame);
            UserOwnedGameRecord ownedGame = registerGameIfNotOwnedElseRetrieve(user, gameRecord);

            if (steamGame.getPlaytimeForever() > 0) {
                GamePlaytimeRecord playtimeRecord = isInitialPlaytimeStats
                        ? gameService.saveInitialPlaytimeRecord(steamGame, gameRecord, user)
                        : gameService.saveDailyPlaytimeRecord(steamGame, gameRecord, user);
                if (playtimeRecord != null)
                    updateOwnedGamePlaytime(ownedGame, playtimeRecord.getPlaytimeForever());
            }
        }
    }

    public UserOwnedGameRecord registerNewOwnedGameForUser(AppUser user, GameRecord game) {
        UserOwnedGameRecord ownedGameRecord = new UserOwnedGameRecord(user, game);
        return userOwnedGameRecordRepository.save(ownedGameRecord);
    }

    /**
     * Registers a game for a user if they do not already own it, otherwise
     * retrieves the existing record.
     * 
     * @param user user to register game for
     * @param game game to register
     * @return the ownership record for the user and game
     */
    public UserOwnedGameRecord registerGameIfNotOwnedElseRetrieve(AppUser user, GameRecord game) {
        if (!userOwnsGame(user, game)) {
            return registerNewOwnedGameForUser(user, game);
        }
        return userOwnedGameRecordRepository.findByAppUserAndGameRecord(user, game);
    }

    public boolean userOwnsGame(AppUser user, GameRecord game) {
        return userOwnedGameRecordRepository.existsByAppUserAndGameRecord(user, game);
    }

    private UserOwnedGameRecord updateOwnedGamePlaytime(UserOwnedGameRecord ownedGame, int newPlaytime) {
        // check if game played recently by comparing its new time with the stored time
        if (ownedGame.getPlaytime() != newPlaytime) {
            ownedGame.setPlaytime(newPlaytime);
        }
        return userOwnedGameRecordRepository.save(ownedGame);
    }
}
