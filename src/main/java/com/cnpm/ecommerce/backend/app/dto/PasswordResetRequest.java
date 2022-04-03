package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.validation.ValidEmail;

import javax.validation.constraints.NotBlank;

public class PasswordResetRequest {

    @NotBlank(message = "is required")
    @ValidEmail
    private String email;

    public PasswordResetRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
