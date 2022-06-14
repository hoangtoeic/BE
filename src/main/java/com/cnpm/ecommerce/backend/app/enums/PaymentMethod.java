package com.cnpm.ecommerce.backend.app.enums;

public enum PaymentMethod {
    CASH(0),
    PAYPAL(1),
    PAYPAL_WEB(2);

    private int method;

    PaymentMethod(int method) { this.method = method; }
}
