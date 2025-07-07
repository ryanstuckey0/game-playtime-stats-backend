package com.ryanstuckey0.stat_saga_backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ryanstuckey0.stat_saga_backend.model.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
    public boolean existsBySteamAppId(Long steamAppId);

    public Optional<Game> findBySteamAppId(Long steamAppId);
}
