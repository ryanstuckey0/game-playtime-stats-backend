package com.stucko09.statsaga.model.steam;

import lombok.Data;

@Data
public class SteamResponse<T> {
    private T response;
}
