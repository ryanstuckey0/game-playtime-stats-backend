package com.statsaga.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.statsaga.backend.model.entity.AppUser;
import com.statsaga.backend.model.entity.Game;
import com.statsaga.backend.model.entity.OwnedGame;

public interface OwnedGameRepository extends CrudRepository<OwnedGame, Long> {
    public List<OwnedGame> findByAppUser(AppUser appUser);

    public boolean existsByAppUserAndGame(AppUser appUser, Game game);

    public OwnedGame findByAppUserAndGame(AppUser appUser, Game game);
}
