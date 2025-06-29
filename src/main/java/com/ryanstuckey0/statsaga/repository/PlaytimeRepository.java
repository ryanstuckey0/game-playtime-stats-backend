package com.ryanstuckey0.statsaga.repository;

import org.springframework.data.repository.CrudRepository;

import com.ryanstuckey0.statsaga.model.entity.AppUser;
import com.ryanstuckey0.statsaga.model.entity.Playtime;
import com.ryanstuckey0.statsaga.model.entity.Game;

public interface PlaytimeRepository extends CrudRepository<Playtime, Long> {
    public boolean existsByAppUser(AppUser appUser);

    public boolean existsByGameAndAppUser(Game game, AppUser appUser);

    public Playtime findFirstByGameAndAppUserOrderByCreationTimestampDesc(Game game,
            AppUser appUser);
}
