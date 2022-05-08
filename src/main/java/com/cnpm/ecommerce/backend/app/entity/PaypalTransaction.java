package com.cnpm.ecommerce.backend.app.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "paypal_transaction")
public class PaypalTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false, unique = true)
    private String paymentId;

    @Column(name = "payload", columnDefinition = "text")
    private String payload;

    @Column(name ="createddate")
    @CreatedDate
    private Date createdDate;

    public PaypalTransaction() {
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPaymentId() { return paymentId; }

    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getPayload() { return payload; }

    public void setPayload(String payload) { this.payload = payload; }

    public Date getCreatedDate() { return createdDate; }

    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
