package com.cnpm.ecommerce.backend.app.dto;

import javax.validation.constraints.NotNull;

public class LogoutRequest {

    @NotNull(message = "is required")
    private String token;

    @NotNull(message = "is required")
    private String refreshToken;

    @NotNull(message = "is required")
    private Long userId;

    public LogoutRequest() {
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
