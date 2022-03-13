package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICartService {
    List<Cart> findAll();

    Page<Cart> findAllPageAndSort(Pageable pagingSort);

    Cart findById(Long theId);

    List<Cart> findByCustomerId(long customerId);

    MessageResponse createCart(CartDTO cartDTO);

    MessageResponse updateCart(Long theId, CartDTO cartDTO);

    void deleteCart(Long theId);

    Page<Cart> findByIdContaining(Long id, Pageable pagingSort);

    Page<Cart> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort);
}
