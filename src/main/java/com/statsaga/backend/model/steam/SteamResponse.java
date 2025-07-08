package com.statsaga.backend.model.steam;

import lombok.Data;

@Data
public class SteamResponse<T> {
    private T response;
}
