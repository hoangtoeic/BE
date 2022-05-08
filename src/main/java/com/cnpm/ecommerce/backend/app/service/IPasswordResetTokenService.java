package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.entity.PasswordResetToken;

import java.util.Optional;

public interface IPasswordResetTokenService {

    Optional<PasswordResetToken> findByToken(String token);

    boolean verifyExpiration(PasswordResetToken token);

}
