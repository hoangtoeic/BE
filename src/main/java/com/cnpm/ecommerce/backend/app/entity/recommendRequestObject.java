package com.cnpm.ecommerce.backend.app.entity;

import java.util.List;

public class recommendRequestObject {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Long> getExceptProductID() {
        return exceptProductID;
    }

    public void setExceptProductID(List<Long> exceptProductID) {
        this.exceptProductID = exceptProductID;
    }

    private List<Long> exceptProductID;

}
