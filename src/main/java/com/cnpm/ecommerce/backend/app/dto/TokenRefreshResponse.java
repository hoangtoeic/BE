package com.cnpm.ecommerce.backend.app.dto;

import java.util.List;

public class TokenRefreshResponse extends JwtResponse{

    public TokenRefreshResponse(String token, String refreshToken, Long id, String username, String email) {
        super(token, refreshToken, id, username, email);
    }
}
