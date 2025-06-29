package com.ryanstuckey0.statsaga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryanstuckey0.statsaga.config.SteamProperties;
import com.ryanstuckey0.statsaga.model.steam.SteamGetRecentGamesResponse;
import com.ryanstuckey0.statsaga.model.steam.SteamResponse;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class SteamApiService {
        private static final String GET_OWNED_GAMES_REQUEST_PATH = "/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&format=json&include_appinfo=true";
        private static final String GET_RECENT_GAMES_REQUEST_PATH = "/IPlayerService/GetRecentlyPlayedGames/v0001/?key=%s&steamid=%s&format=json&include_appinfo=true";

        @Autowired
        private SteamProperties steamProperties;

        @Autowired
        private RestTemplate restTemplate;

        public SteamResponse<SteamGetRecentGamesResponse> getOwnedGames(Long steamId, String apiKey) {
                return makeRequestToSteam(steamId, apiKey, GET_OWNED_GAMES_REQUEST_PATH,
                                new ParameterizedTypeReference<SteamResponse<SteamGetRecentGamesResponse>>() {
                                });
        }

        public SteamResponse<SteamGetRecentGamesResponse> getRecentlyPlayedGames(Long steamId, String apiKey) {
                return makeRequestToSteam(steamId, apiKey, GET_RECENT_GAMES_REQUEST_PATH,
                                new ParameterizedTypeReference<SteamResponse<SteamGetRecentGamesResponse>>() {
                                });
        }

        private <T> SteamResponse<T> makeRequestToSteam(Long steamId, String apiKey, String requestPath,
                        ParameterizedTypeReference<SteamResponse<T>> responseType) {
                RequestEntity<Void> requestEntity = RequestEntity
                                .get(steamProperties.getHostname()
                                                + String.format(requestPath, apiKey, steamId))
                                .accept(MediaType.APPLICATION_JSON).build();
                ResponseEntity<SteamResponse<T>> response = restTemplate.exchange(requestEntity, responseType);

                return response.getBody();
        }
}
