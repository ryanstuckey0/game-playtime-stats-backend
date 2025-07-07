package com.ryanstuckey0.stat_saga_backend.model.steam;

import lombok.Data;

@Data
public class SteamResponse<T> {
    private T response;
}
