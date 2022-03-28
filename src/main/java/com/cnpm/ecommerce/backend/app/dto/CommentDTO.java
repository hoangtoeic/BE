package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties({"product", "customerDTO"})
public class CommentDTO extends AbstractDTO {

    private String comment;

    private Product product;

    @NotNull(message = "Please input product id")
    private Long productId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private CustomerDTO customerDTO;

    @NotNull(message = "Please input customer id")
    private Long customerId;


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