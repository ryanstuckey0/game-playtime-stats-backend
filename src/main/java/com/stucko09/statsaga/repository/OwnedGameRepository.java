package com.stucko09.statsaga.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.statsaga.model.entity.AppUser;
import com.stucko09.statsaga.model.entity.Game;
import com.stucko09.statsaga.model.entity.OwnedGame;

public interface OwnedGameRepository extends CrudRepository<OwnedGame, Long> {
    public List<OwnedGame> findByAppUser(AppUser appUser);

    public boolean existsByAppUserAndGame(AppUser appUser, Game game);

    public OwnedGame findByAppUserAndGame(AppUser appUser, Game game);
}
