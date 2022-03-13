package com.cnpm.ecommerce.backend.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity{

    @Column(name = "rating")
    private int rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Feedback() {
    }
}
