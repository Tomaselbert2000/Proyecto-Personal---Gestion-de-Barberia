package com.repository;

import com.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByUsernameAndUserIdNot(String username, Long userId);

    Boolean existsByUsernameAndPassword(String username, String password);

    AppUser findByUsername(String username);
}
