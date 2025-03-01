package com.stucko09.steam_aggregator.repository;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.steam_aggregator.model.entity.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    public AppUser findBySteamUserId(Long steamUserId);
    public AppUser findByUsername(String username);
    public boolean existsBySteamUserId(Long steamUserId);
    public boolean existsByUsername(String username);
}
