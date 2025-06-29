package com.ryanstuckey0.statsaga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanstuckey0.statsaga.model.entity.AppUser;
import com.ryanstuckey0.statsaga.model.entity.Playtime;
import com.ryanstuckey0.statsaga.model.entity.Game;
import com.ryanstuckey0.statsaga.model.entity.OwnedGame;
import com.ryanstuckey0.statsaga.model.steam.SteamGamePlaytimeRecord;
import com.ryanstuckey0.statsaga.model.steam.SteamGetRecentGamesResponse;
import com.ryanstuckey0.statsaga.repository.OwnedGameRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserStatsService {

    @Autowired
    private SteamApiService steamApiService;

    @Autowired
    private GameService gameService;

    @Autowired
    private OwnedGameRepository ownedGameRepository;

    public void collectAndSaveInitialPlaytimeStats(AppUser user) {
        SteamGetRecentGamesResponse ownedGamesResponse = steamApiService
                .getOwnedGames(user.getSteamUserId(), user.getApiKey())
                .getResponse();
        log.debug("Retrieved owned games for user and saving initial playtime stats. Count: {}",
                () -> ownedGamesResponse.getGames().size());
        registerGamesAndSavePlaytimeStats(user, ownedGamesResponse, true);
    }

    public void collectAndSaveDailyPlaytimeStats(AppUser user) {
        SteamGetRecentGamesResponse recentGamesResponse = steamApiService
                .getRecentlyPlayedGames(user.getSteamUserId(), user.getApiKey())
                .getResponse();
        log.debug("Retrieved recent games for user and saving playtime stats. Count: {}",
                () -> recentGamesResponse.getGames().size());
        registerGamesAndSavePlaytimeStats(user, recentGamesResponse, false);
    }

    private void registerGamesAndSavePlaytimeStats(AppUser user, SteamGetRecentGamesResponse recentGamesResponse,
            boolean isInitialPlaytimeStats) {
        for (SteamGamePlaytimeRecord steamGame : recentGamesResponse.getGames()) {
            Game game = gameService.saveOrRetrieveGame(steamGame);
            OwnedGame ownedGame = registerGameIfNotOwnedElseRetrieve(user, game);

            if (steamGame.getPlaytimeForever() > 0) {
                Playtime playtimeRecord = isInitialPlaytimeStats
                        ? gameService.saveInitialPlaytimeRecord(steamGame, game, user)
                        : gameService.saveDailyPlaytimeRecord(steamGame, game, user);
                if (playtimeRecord != null)
                    updateOwnedGamePlaytime(ownedGame, playtimeRecord.getPlaytimeForever());
            }
        }
    }

    public OwnedGame registerNewOwnedGameForUser(AppUser user, Game game) {
        OwnedGame ownedGame = new OwnedGame(user, game);
        return ownedGameRepository.save(ownedGame);
    }

    /**
     * Registers a game for a user if they do not already own it, otherwise
     * retrieves the existing record.
     * 
     * @param user user to register game for
     * @param game game to register
     * @return the ownership record for the user and game
     */
    public OwnedGame registerGameIfNotOwnedElseRetrieve(AppUser user, Game game) {
        if (!userOwnsGame(user, game)) {
            return registerNewOwnedGameForUser(user, game);
        }
        return ownedGameRepository.findByAppUserAndGame(user, game);
    }

    public boolean userOwnsGame(AppUser user, Game game) {
        return ownedGameRepository.existsByAppUserAndGame(user, game);
    }

    private OwnedGame updateOwnedGamePlaytime(OwnedGame ownedGame, int newPlaytime) {
        // check if game played recently by comparing its new time with the stored time
        if (ownedGame.getPlaytime() != newPlaytime) {
            ownedGame.setPlaytime(newPlaytime);
        }
        return ownedGameRepository.save(ownedGame);
    }
}
