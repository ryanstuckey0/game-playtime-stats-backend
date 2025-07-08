package com.statsaga.backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.statsaga.backend.model.entity.AppUser;
import com.statsaga.backend.model.entity.Game;
import com.statsaga.backend.model.entity.Playtime;

public interface PlaytimeRepository extends CrudRepository<Playtime, Long> {
    public boolean existsByAppUser(AppUser appUser);

    public boolean existsByGameAndAppUser(Game game, AppUser appUser);

    public Playtime findFirstByGameAndAppUserOrderByCreationTimestampDesc(Game game,
            AppUser appUser);
}
