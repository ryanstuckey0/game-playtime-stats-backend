package com.ryanstuckey0.statsaga.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "steam")
public class SteamProperties {
    private String hostname;
}
