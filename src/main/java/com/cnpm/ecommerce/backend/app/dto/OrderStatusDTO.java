package com.cnpm.ecommerce.backend.app.dto;

import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.validation.EnumNamePattern;

import javax.validation.constraints.NotNull;

public class OrderStatusDTO {

    @NotNull(message = "is required")
    private Long userId;

    @NotNull(message = "is required")
    @EnumNamePattern(regexp = "CANCELLED|REFUNDED|COMPLETED|PAID|DECLINED|PENDING")
    private OrderStatus status;

    public OrderStatusDTO() {
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }
}
