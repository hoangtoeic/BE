package com.cnpm.ecommerce.backend.app.dto;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CartTempDTO extends AbstractDTO {

    @NotNull(message = "is required")
    private long customerId;

    @NotNull(message = "is required")
    private Long productId;

    @NotNull(message = "is required")
    private Integer quantity;

    @NotNull(message = "is required")
    private BigDecimal salePrice;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalePrice() { return salePrice; }

    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public CartTempDTO() {
    }
}