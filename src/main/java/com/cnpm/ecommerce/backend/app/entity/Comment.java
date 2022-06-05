package com.cnpm.ecommerce.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name= "Comment")
@JsonIgnoreProperties({"product"})

public class Comment extends BaseEntity {
    @Column(name = "comment")
    private String comment;

    public Long getProductIds() {
        return productIds;
    }

    public void setProductIds(Long productIds) {
        this.productIds = productIds;
    }

    public Long getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(Long customerIds) {
        this.customerIds = customerIds;
    }

    @Transient
    @JsonProperty(value = "productId")
    private Long productIds;

    @Transient
    @JsonProperty(value = "customerId")
    private Long customerIds;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User customer;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
}
