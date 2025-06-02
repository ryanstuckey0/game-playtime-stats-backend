package com.stucko09.steam_aggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.stucko09.steam_aggregator.model.entity.AppUser;
import com.stucko09.steam_aggregator.service.UserService;
import com.stucko09.steam_aggregator.service.UserStatsService;

@RestController
public class FunctionalityTestingController {
    @Autowired
    private UserStatsService statsCollectionService;

    @Autowired
    private UserService userService;

    @GetMapping("/saveInitialPlaytime/{steamId}")
    public void saveInitialPlaytime(@RequestHeader String apiKey, @PathVariable Long steamId) {
        AppUser user = userService.retrieveUser(steamId);
        user.setApiKey(apiKey);
        statsCollectionService.collectAndSaveInitialPlaytimeStats(user);
    }

    @GetMapping("/saveDailyPlaytime/{steamId}")
    public void saveDailyPlaytime(@RequestHeader String apiKey, @PathVariable Long steamId) {
        AppUser user = userService.retrieveUser(steamId);
        user.setApiKey(apiKey);
        statsCollectionService.collectAndSaveDailyPlaytimeStats(user);
    }
}
