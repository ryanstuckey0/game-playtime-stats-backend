package com.ryanstuckey0.stat_saga_backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.ryanstuckey0.stat_saga_backend.model.entity.AppUser;
import com.ryanstuckey0.stat_saga_backend.model.entity.Game;
import com.ryanstuckey0.stat_saga_backend.model.entity.Playtime;

public interface PlaytimeRepository extends CrudRepository<Playtime, Long> {
    public boolean existsByAppUser(AppUser appUser);

    public boolean existsByGameAndAppUser(Game game, AppUser appUser);

    public Playtime findFirstByGameAndAppUserOrderByCreationTimestampDesc(Game game,
            AppUser appUser);
}
