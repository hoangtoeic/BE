package com.cnpm.ecommerce.backend.app.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DeleteDTO {
    @NotNull(message = "is required")
    private List<Long> ids = new ArrayList<>();

    public DeleteDTO() {
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
