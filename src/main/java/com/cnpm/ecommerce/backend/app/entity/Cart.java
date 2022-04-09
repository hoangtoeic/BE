package com.cnpm.ecommerce.backend.app.entity;


import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@JsonIgnoreProperties({"user"})
public class Cart extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "note")
    private String note;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Transient
    @JsonProperty(value = "userId")
    private Long userIds;

    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"cart"})
    private List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Long getUserIds() { return userIds; }

    public void setUserIds(Long userIds) { this.userIds = userIds; }
}
