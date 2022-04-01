package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.RefreshToken;
import com.cnpm.ecommerce.backend.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
