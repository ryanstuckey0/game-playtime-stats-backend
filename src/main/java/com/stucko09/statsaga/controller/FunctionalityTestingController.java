package com.stucko09.statsaga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stucko09.statsaga.model.entity.AppUser;
import com.stucko09.statsaga.model.rest.GenericResponse;
import com.stucko09.statsaga.service.UserService;
import com.stucko09.statsaga.service.UserStatsService;

@Profile("!prod")
@RequestMapping("/test")
@RestController
public class FunctionalityTestingController {
    @Autowired
    private UserStatsService statsCollectionService;

    @Autowired
    private UserService userService;

    @GetMapping("saveInitialPlaytime/{steamId}")
    public GenericResponse saveInitialPlaytime(@RequestHeader String apiKey, @PathVariable Long steamId) {
        AppUser user = userService.retrieveUser(steamId);
        user.setApiKey(apiKey);
        statsCollectionService.collectAndSaveInitialPlaytimeStats(user);
        return new GenericResponse("Collected initial stats for user.", true);
    }

    @GetMapping("saveDailyPlaytime/{steamId}")
    public GenericResponse saveDailyPlaytime(@RequestHeader String apiKey, @PathVariable Long steamId) {
        AppUser user = userService.retrieveUser(steamId);
        user.setApiKey(apiKey);
        statsCollectionService.collectAndSaveDailyPlaytimeStats(user);
        return new GenericResponse("Collected daily stats for user.", true);
    }
}
