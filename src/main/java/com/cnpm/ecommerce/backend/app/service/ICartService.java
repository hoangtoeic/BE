package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.OrderStatusDTO;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICartService {

    Page<Cart> findAllPageAndSort(Pageable pagingSort);

    Cart findById(Long theId);

    MessageResponse createCart(CartDTO cartDTO);

//    MessageResponse updateCart(Long theId, CartDTO cartDTO);

    void deleteCart(Long theId);

    Page<Cart> findByIdContaining(Long id, Pageable pagingSort);

    Page<Cart> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort);

    MessageResponse updateStatusCart(long theId, OrderStatusDTO statusDto);

    Page<Cart> findByPaymentAndStatus(String paymentType, String status, Pageable pagingSort);

    Page<Cart> findByCustomerNamePaymentAndStatusPageAndSort(String customerName, String paymentType, String status, Pageable pagingSort);
}
