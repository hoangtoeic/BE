package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT c from CartItem c WHERE c.cart.id=?1")
    List<CartItem> findCartItemByCartID(Long cartId);

    Page<CartItem> findById(Long id, Pageable pageable);

    Page<CartItem> findByCartId(Long cartId, Pageable pageable);
}
