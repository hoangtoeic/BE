package com.cnpm.ecommerce.backend.app.entity;

import java.util.List;

public class recommendRequestObject {
    private Integer id;
    private List<Integer> exceptProductID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getExceptProductID() {
        return exceptProductID;
    }

    public void setExceptProductID(List<Integer> exceptProductID) {
        this.exceptProductID = exceptProductID;
    }
}
