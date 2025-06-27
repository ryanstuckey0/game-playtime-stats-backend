package com.stucko09.statsaga.repository;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.statsaga.model.entity.AppUser;
import com.stucko09.statsaga.model.entity.Playtime;
import com.stucko09.statsaga.model.entity.Game;

public interface GamePlaytimeRecordRepository extends CrudRepository<Playtime, Long> {
    public boolean existsByAppUser(AppUser appUser);

    public boolean existsByGameAndAppUser(Game game, AppUser appUser);

    public Playtime findFirstByGameAndAppUserOrderByCreationTimestampDesc(Game game,
            AppUser appUser);
}
