package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.CartTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartTempRepository extends JpaRepository<CartTemp, Long> {

    @Query("SELECT c from CartTemp c WHERE c.customer.id=?1")
    List<CartTemp> findByCustomer(Long customerId);

    Page<CartTemp> findById(Long id, Pageable pageable);

    Page<CartTemp> findByCustomerId(Long customerId, Pageable pageable);

    Optional<CartTemp> findByCustomerIdAndProductId(long customerId, Long productId);

}

