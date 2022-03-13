package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartItemDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICartItemService {
    List<CartItem> findAll();

    Page<CartItem> findAllPageAndSort(Pageable pagingSort);

    CartItem findById(Long theId);

    List<CartItem> findByCartId(Long cartId);

    MessageResponse createCartItem(CartItemDTO CartItemDTO);

    MessageResponse updateCartItem(Long theId, CartItemDTO CartItemDTO);

    void deleteCartItem(Long theId);

    Page<CartItem> findByIdContaining(Long id, Pageable pagingSort);

    Page<CartItem> findByCartIdPageAndSort(Long cartId, Pageable pagingSort);
}
