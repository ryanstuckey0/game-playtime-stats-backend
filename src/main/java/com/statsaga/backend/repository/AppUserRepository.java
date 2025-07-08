package com.statsaga.backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.statsaga.backend.model.entity.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    public AppUser findBySteamUserId(Long steamUserId);

    public AppUser findByUsername(String username);

    public boolean existsBySteamUserId(Long steamUserId);

    public boolean existsByUsername(String username);
}
