package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartTempDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICartTempService {

    Page<CartTemp> findAllPageAndSort(Pageable pagingSort);

    CartTemp findById(Long theId);

    MessageResponse createCartTemp(CartTempDTO cartTempDTO);

    MessageResponse updateCartTemp(Long theId, CartTempDTO cartTempDTO);

    MessageResponse deleteCart(Long theId);

    MessageResponse deleteMultipleCart(List<Long> theIds);

    Page<CartTemp> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort);

}
