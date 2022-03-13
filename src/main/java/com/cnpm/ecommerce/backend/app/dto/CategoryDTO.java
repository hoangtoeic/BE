package com.cnpm.ecommerce.backend.app.dto;

import javax.validation.constraints.NotNull;

public class CategoryDTO extends AbstractDTO{

    @NotNull(message = "Name is required")
    private String name;

    private String description;

    private String thumbnail;

    public CategoryDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
