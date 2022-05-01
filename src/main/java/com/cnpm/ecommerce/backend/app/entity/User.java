package com.cnpm.ecommerce.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints =
        {@UniqueConstraint(columnNames = "userName"),
         @UniqueConstraint(columnNames = "email")})
@JsonIgnoreProperties({"roles", "password", "feedbacks", "accCustomer", "profilePictureArr"})
public class User extends BaseEntity{

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private int gender;

    @Column(name = "profile_picture", length = 100000)
    @Lob
    private byte[] profilePictureArr;

    @Transient
    private String profilePicture;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "is_acc_customer")
    private Boolean isAccCustomer;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private Set<Feedback> feedbacks = new HashSet<>();

    @Transient
    private String roleCode;

    public User() {
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

    public Set<Feedback> getFeedbacks() { return feedbacks; }

    public void setFeedbacks(Set<Feedback> feedbacks) { this.feedbacks = feedbacks; }

    public Boolean getAccCustomer() { return isAccCustomer; }

    public void setAccCustomer(Boolean accCustomer) { isAccCustomer = accCustomer; }

    public byte[] getProfilePictureArr() { return profilePictureArr; }

    public void setProfilePictureArr(byte[] profilePictureArr) { this.profilePictureArr = profilePictureArr; }

    public String getRoleCode() { return roleCode; }

    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
}
