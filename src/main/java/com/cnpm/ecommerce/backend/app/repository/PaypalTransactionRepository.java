package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.PaypalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaypalTransactionRepository extends JpaRepository<PaypalTransaction, Long> {

    Optional<PaypalTransaction> findByPaymentId(String paymentId);
}
