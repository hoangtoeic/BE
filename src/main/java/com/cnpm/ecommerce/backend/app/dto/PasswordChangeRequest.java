package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.validation.ValidEmail;

import javax.validation.constraints.NotBlank;

public class PasswordChangeRequest {

    @NotBlank(message = "is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "is required.")
    private String oldPassword;

    @NotBlank(message = "is required.")
    private String password;

    public PasswordChangeRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() { return oldPassword; }

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
