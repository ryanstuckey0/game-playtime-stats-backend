package com.stucko09.statsaga.repository;

import org.springframework.data.repository.CrudRepository;

import com.stucko09.statsaga.model.entity.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    public AppUser findBySteamUserId(Long steamUserId);

    public AppUser findByUsername(String username);

    public boolean existsBySteamUserId(Long steamUserId);

    public boolean existsByUsername(String username);
}
