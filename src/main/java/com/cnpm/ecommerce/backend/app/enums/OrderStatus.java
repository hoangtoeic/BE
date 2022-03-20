package com.cnpm.ecommerce.backend.app.enums;

public enum OrderStatus {
    PENDING(1),
    COMPLETED(2),
    REFUNDED(3),
    CANCELLED(4),
    DECLINED(5),
    PAID(6);
    private int status;

    OrderStatus(int status) {
        this.status = status;
    }
}
