package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.LogoutRequest;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Blacklist;
import com.cnpm.ecommerce.backend.app.entity.RefreshToken;
import com.cnpm.ecommerce.backend.app.repository.BlacklistRepository;
import com.cnpm.ecommerce.backend.app.repository.RefreshTokenRepository;
import com.cnpm.ecommerce.backend.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class BlacklistService implements IBlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<Blacklist> findByToken(String token) {
        return blacklistRepository.findByToken(token);
    }

    @Override
    public MessageResponse addTokenToBlacklist(LogoutRequest logoutRequest) {
        if(!blacklistRepository.findByToken(logoutRequest.getToken()).isPresent() ){
            Blacklist blacklist = new Blacklist();
            blacklist.setUser(userRepository.findById(logoutRequest.getUserId()).get());
            blacklist.setToken(logoutRequest.getToken());

            blacklistRepository.save(blacklist);

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(logoutRequest.getRefreshToken());

            if(refreshToken.isPresent()) {
                refreshTokenRepository.delete(refreshToken.get());
            } else {
                return new MessageResponse("Refresh token is incorrect!", HttpStatus.OK, LocalDateTime.now());
            }

            return new MessageResponse("Logout successfully!", HttpStatus.OK, LocalDateTime.now());
        }
        return new MessageResponse("Exist token in database", HttpStatus.OK, LocalDateTime.now());
    }
}
