package com.cnpm.ecommerce.backend.app.dto;


import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.enums.PaymentMethod;
import com.cnpm.ecommerce.backend.app.validation.EnumNamePattern;
import com.cnpm.ecommerce.backend.app.validation.ValueOfEnum;
import com.cnpm.ecommerce.backend.app.validationgroups.OnUpdate;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDTO extends AbstractDTO {

    @NotNull(message = "is required")
    private long customerId;

    private String note;

    @NotNull(message = "is required")
    private BigDecimal totalCost;

    @NotNull(message = "is required")
    private String address;

    @NotNull(message = "is required", groups = {OnUpdate.class})
    @EnumNamePattern(regexp = "CANCELLED|REFUNDED|COMPLETED|PAID|DECLINED|PENDING")
    private OrderStatus status;

    @NotNull(message = "is required")
    @EnumNamePattern(regexp = "CASH|PAYPAL|PAYPAL_WEB")
    private PaymentMethod paymentMethod;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {  this.status = status; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}