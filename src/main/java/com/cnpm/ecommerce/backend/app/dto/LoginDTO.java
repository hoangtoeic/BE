package com.cnpm.ecommerce.backend.app.dto;

import javax.validation.constraints.NotNull;

public class LoginDTO {

    @NotNull(message = "is required")
    private String userName;

    @NotNull(message = "is required")
    private String password;

    public LoginDTO() {
        super();
    }

    public LoginDTO(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
