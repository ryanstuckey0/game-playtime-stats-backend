package com.ryanstuckey0.statsaga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ryanstuckey0.statsaga.model.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
    public boolean existsBySteamAppId(Long steamAppId);

    public Optional<Game> findBySteamAppId(Long steamAppId);
}
