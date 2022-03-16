package com.cnpm.ecommerce.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cnpm.ecommerce.backend.app.entity.Feedback;
import com.cnpm.ecommerce.backend.app.entity.Role;
import com.cnpm.ecommerce.backend.app.utils.Provider;
import com.cnpm.ecommerce.backend.app.validation.ValidEmail;
import com.cnpm.ecommerce.backend.app.validation.ValidPhoneNumber;
import com.cnpm.ecommerce.backend.app.validation.ValidUsername;

import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties({"role", "roleCode", "feedbacks", "provider" })
public class CustomerDTO extends AbstractDTO{

    @ValidUsername
    @NotNull(message = "is required")
    private String userName;

    @NotNull(message = "is required")
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

    private int gender;

    private String profilePicture;

    private int enabled;

    private Provider provider;

    private Role role;

    private String roleCode;

    private Set<Feedback> feedbacks;

    public CustomerDTO() {
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
