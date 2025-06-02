package com.stucko09.steam_aggregator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.stucko09.steam_aggregator.exception.UserDoesNotExistException;
import com.stucko09.steam_aggregator.exception.UsernameTakenException;
import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.model.rest.UserOwnedGamesResponse;
import com.stucko09.steam_aggregator.model.rest.UserRegistrationRequest;
import com.stucko09.steam_aggregator.repository.AppUserRepository;
import com.stucko09.steam_aggregator.repository.UserOwnedGameRecordRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserStatsService statsCollectionService;

    @Autowired
    private UserOwnedGameRecordRepository userOwnedGameRecordRepository;

    @Autowired
    private Mapper dozerBeanMapper;

    public AppUser registerUserAndSaveInitialPlaytime(UserRegistrationRequest userRegistrationRequest, String apiKey)
            throws UsernameTakenException {
        AppUser user = registerUser(userRegistrationRequest);
        if (user.getSteamUserId() != null) {
            user.setApiKey(apiKey);
            statsCollectionService.collectAndSaveInitialPlaytimeStats(user);
            log.info("User registered and initial playtime stats collected.");
        }
        return user;
    }

    public AppUser retrieveUser(Long steamId) {
        AppUser user = appUserRepository.findBySteamUserId(steamId);
        if (user == null)
            throw new UserDoesNotExistException(steamId);
        return user;
    }

    public List<UserOwnedGamesResponse> getOwnedGamesForUser(AppUser user) {
        return userOwnedGameRecordRepository
                .findByAppUser(user)
                .stream()
                .map(
                        ownedGame -> {
                            return dozerBeanMapper.map(ownedGame, UserOwnedGamesResponse.class);
                        })
                .toList();
    }

    public boolean userIsRegistered(Long steamId) {
        return appUserRepository.existsBySteamUserId(steamId);
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser registerUser(UserRegistrationRequest request) throws UsernameTakenException {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UsernameTakenException(request.getUsername());
        }
        return appUserRepository.save(new AppUser(request));
    }
}
