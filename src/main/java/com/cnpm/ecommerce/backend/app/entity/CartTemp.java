package com.cnpm.ecommerce.backend.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_temp")
@JsonIgnoreProperties({"product", "customer"})
public class CartTemp extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"product"})
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Transient
    @JsonProperty(value = "customerId")
    private Long customerIds;

    @Transient
    @JsonProperty(value = "productId")
    private Long productIds;

    public CartTemp() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalePrice() { return salePrice; }

    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public Long getProductIds() { return productIds; }

    public void setProductIds(Long productIds) { this.productIds = productIds; }

    public Long getCustomerIds() { return customerIds; }

    public void setCustomerIds(Long customerIds) { this.customerIds = customerIds; }

    public User getCustomer() { return customer; }

    public void setCustomer(User customer) { this.customer = customer; }
}
