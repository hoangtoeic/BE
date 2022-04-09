package com.cnpm.ecommerce.backend.app.dto;


import com.cnpm.ecommerce.backend.app.validationgroups.OnUpdate;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItemDTO {

    private Long cartId;

    @NotNull(message = "is required")
    private Long productId;

    @NotNull(message = "is required")
    private Integer quantity;

    @NotNull(message = "is required")
    private BigDecimal salePrice;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalePrice() { return salePrice; }

    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    @NotNull(message = "is required", groups = {OnUpdate.class})
    private Long id;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private String createdBy;

    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}