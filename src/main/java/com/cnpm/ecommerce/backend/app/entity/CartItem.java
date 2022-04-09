package com.cnpm.ecommerce.backend.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@JsonIgnoreProperties({"product", "cart"})
public class CartItem extends BaseEntity{
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties({"cartItems"})
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"product"})
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Transient
    @JsonProperty(value = "cartId")
    private Long cartIds;

    @Transient
    @JsonProperty(value = "productId")
    private Long productIds;


    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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

    public Long getCartIds() { return cartIds; }

    public void setCartIds(Long cartIds) { this.cartIds = cartIds; }

    public Long getProductIds() { return productIds; }

    public void setProductIds(Long productIds) { this.productIds = productIds; }
}
