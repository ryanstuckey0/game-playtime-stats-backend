package com.ryanstuckey0.statsaga.controller;

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

import com.ryanstuckey0.statsaga.exception.UsernameTakenException;
import com.ryanstuckey0.statsaga.model.entity.AppUser;
import com.ryanstuckey0.statsaga.model.rest.GenericResponse;
import com.ryanstuckey0.statsaga.model.rest.OwnedGamesResponse;
import com.ryanstuckey0.statsaga.model.rest.UserRegistrationRequest;
import com.ryanstuckey0.statsaga.model.rest.UserRegistrationResponse;
import com.ryanstuckey0.statsaga.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{username}/games")
    public List<OwnedGamesResponse> getGamesForUser(@PathVariable String username) {
        AppUser user = userService.getUserByUsername(username);
        return userService.getOwnedGamesForUser(user);
    }

    @PostMapping("/register")
    public UserRegistrationResponse registerUser(@RequestBody UserRegistrationRequest requestBody)
            throws UsernameTakenException {
        log.debug("Registering new user with username: {}", requestBody.getUsername());
        AppUser user = userService.registerUser(requestBody);
        // TODO: API or DB write here to tell batch service to collect initial playtime
        // stats
        return new UserRegistrationResponse(user.getId());
    }

    @ExceptionHandler({ UsernameTakenException.class })
    public ResponseEntity<GenericResponse> handleUsernameTakenException(UsernameTakenException e) {
        return ResponseEntity.badRequest().body(new GenericResponse(e.getMessage(), false));
    }
}
