package com.stucko09.statsaga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.statsaga.model.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
    public boolean existsBySteamAppId(Long steamAppId);

    public Optional<Game> findBySteamAppId(Long steamAppId);
}
