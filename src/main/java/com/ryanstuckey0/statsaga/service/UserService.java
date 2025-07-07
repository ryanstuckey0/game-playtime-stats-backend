package com.ryanstuckey0.statsaga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.ryanstuckey0.statsaga.exception.UserDoesNotExistException;
import com.ryanstuckey0.statsaga.exception.UsernameTakenException;
import com.ryanstuckey0.statsaga.model.entity.AppUser;
import com.ryanstuckey0.statsaga.model.rest.OwnedGamesResponse;
import com.ryanstuckey0.statsaga.model.rest.UserRegistrationRequest;
import com.ryanstuckey0.statsaga.repository.AppUserRepository;
import com.ryanstuckey0.statsaga.repository.OwnedGameRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private OwnedGameRepository ownedGameRepository;

    @Autowired
    private Mapper dozerBeanMapper;

    public AppUser registerUser(UserRegistrationRequest request) throws UsernameTakenException {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UsernameTakenException(request.getUsername());
        }
        AppUser user = appUserRepository.save(new AppUser(request));
        log.debug("Saved new user to app_user table with ID: {}", user.getId());
        return user;
    }

    public AppUser retrieveUser(Long steamId) {
        AppUser user = appUserRepository.findBySteamUserId(steamId);
        if (user == null)
            throw new UserDoesNotExistException(steamId);
        return user;
    }

    public List<OwnedGamesResponse> getOwnedGamesForUser(AppUser user) {
        return ownedGameRepository
                .findByAppUser(user)
                .stream()
                .map(
                        ownedGame -> {
                            return dozerBeanMapper.map(ownedGame, OwnedGamesResponse.class);
                        })
                .toList();
    }

    public boolean userIsRegistered(Long steamId) {
        return appUserRepository.existsBySteamUserId(steamId);
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
