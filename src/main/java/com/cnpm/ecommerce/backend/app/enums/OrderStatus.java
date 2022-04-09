package com.cnpm.ecommerce.backend.app.enums;

public enum OrderStatus {
    PENDING(0), // Default CASH method
    COMPLETED(1),
    REFUNDED(2),
    CANCELLED(3),
    DECLINED(4),
    PAID(5); // Default Paypal method
    private int status;

    OrderStatus(int status) {
        this.status = status;
    }
}
