package com.statsaga.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.statsaga.backend.model.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
    public boolean existsBySteamAppId(Long steamAppId);

    public Optional<Game> findBySteamAppId(Long steamAppId);
}
