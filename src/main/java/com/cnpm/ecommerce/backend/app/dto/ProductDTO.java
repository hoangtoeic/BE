package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.entity.Category;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDTO extends AbstractDTO {

    @NotNull(message = "Please enter name")
    private String name;

    private String brand;

    private String shortDescription;

    private String description;

    @NotNull(message = "Please enter price")
    private BigDecimal price;

    @NotNull(message = "Please enter unit in stock")
    private int unitInStock;

    private String thumbnail;

    private Category category;

    private Long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public ProductDTO() {
    }
}
