package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c WHERE c.customer.id=?1")
    List<Cart> findByCustomer(Long customerId);

    Page<Cart> findById(Long id, Pageable pageable);

    Page<Cart> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT SUM(c.totalCost) from Cart c WHERE c.createdDate=?1")
    BigDecimal getAllRevenueByDay(Date day);

    Page<Cart> findByStatus(OrderStatus status, Pageable pageable);

    Page<Cart> findByPaymentMethod(PaymentMethod anEnum, Pageable pagingSort);

    Page<Cart> findByCustomerNameContainingIgnoreCaseAndStatus(String customerName, OrderStatus status, Pageable pageable);

    Page<Cart> findByCustomerNameContainingIgnoreCaseAndPaymentMethod(String customerName, PaymentMethod paymentMethod, Pageable pageable);

    Page<Cart> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pagingSort);

    Page<Cart> findByCustomerNameContainingIgnoreCaseAndPaymentMethodAndStatus(String customerName, PaymentMethod anEnum, OrderStatus anEnum1, Pageable pagingSort);

    Page<Cart> findByPaymentMethodAndStatus(PaymentMethod anEnum, OrderStatus anEnum1, Pageable pagingSort);
}

