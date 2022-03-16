package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties({"product", "customerDTO"})
public class FeedbackDTO extends AbstractDTO{

    private int rating;

    private Product product;

    @NotNull(message = "Please input product id")
    private Long productId;

    private CustomerDTO customerDTO;

    @NotNull(message = "Please input customer id")
    private Long customerId;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
