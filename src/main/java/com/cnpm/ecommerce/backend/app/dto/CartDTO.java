package com.cnpm.ecommerce.backend.app.dto;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDTO extends AbstractDTO {

    @NotNull(message = "is required")
    private long customerId;

    private String note;

    private BigDecimal totalCost;

    @NotNull(message = "is required")
    private String address;


    private List<CartItemDTO> cartItems = new ArrayList<>();

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(cartItems);
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}