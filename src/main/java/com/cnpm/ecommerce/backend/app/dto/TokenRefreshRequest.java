package com.cnpm.ecommerce.backend.app.dto;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {

    @NotBlank(message = "is required")
    private String refreshToken;

    public TokenRefreshRequest() {
    }

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
