package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartItemDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICartItemService {

    Page<CartItem> findAllPageAndSort(Pageable pagingSort);

    CartItem findById(Long theId);

    MessageResponse createCartItem(CartItemDTO CartItemDTO);

    MessageResponse updateCartItem(Long theId, CartItemDTO CartItemDTO);

    void deleteCartItem(Long theId);

    Page<CartItem> findByIdContaining(Long id, Pageable pagingSort);

    Page<CartItem> findByCartIdPageAndSort(Long cartId, Pageable pagingSort);
}
