package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.validationgroups.OnCreate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cnpm.ecommerce.backend.app.entity.Role;
import com.cnpm.ecommerce.backend.app.validation.ValidEmail;
import com.cnpm.ecommerce.backend.app.validation.ValidPhoneNumber;
import com.cnpm.ecommerce.backend.app.validation.ValidUsername;

import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties({"roles"})
public class EmployeeDTO extends AbstractDTO{

    @ValidUsername
    @NotNull(message = "is required")
    private String userName;

    @NotNull(message = "is required", groups = {OnCreate.class})
    private String password;

    @NotNull(message = "is required")
    private String name;

    private String address;

    @NotNull(message = "is required")
    @ValidPhoneNumber
    private String phoneNumber;

    @ValidEmail
    @NotNull(message = "is required")
    private String email;

    @NotNull(message = "is required")
    private int gender;

    private String profilePicture;

    private int enabled;

    private Set<Role> roles;

    @NotNull(message = "is required")
    private String roleCode;

    public EmployeeDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
