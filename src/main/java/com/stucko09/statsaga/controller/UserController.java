package com.stucko09.statsaga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stucko09.statsaga.exception.UsernameTakenException;
import com.stucko09.statsaga.model.entity.AppUser;
import com.stucko09.statsaga.model.rest.GenericResponse;
import com.stucko09.statsaga.model.rest.OwnedGamesResponse;
import com.stucko09.statsaga.model.rest.UserRegistrationRequest;
import com.stucko09.statsaga.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{username}/games")
    public List<UserOwnedGamesResponse> getGamesForUser(@PathVariable String username) {
        AppUser user = userService.getUserByUsername(username);
        return userService.getOwnedGamesForUser(user);
    }

    @PostMapping("/users/register")
    public GenericResponse registerUser(@RequestBody UserRegistrationRequest requestBody)
            throws UsernameTakenException {
        AppUser user = userService.registerUserAndSaveInitialPlaytime(requestBody, requestBody.getSteamApiKey());
        return new GenericResponse("User registered and initial playtime stats collected. New user id: " + user.getId(),
                true);
    }

    @ExceptionHandler({ UsernameTakenException.class })
    public ResponseEntity<GenericResponse> handleUsernameTakenException(UsernameTakenException e) {
        return ResponseEntity.badRequest().body(new GenericResponse(e.getMessage(), false));
    }
}
