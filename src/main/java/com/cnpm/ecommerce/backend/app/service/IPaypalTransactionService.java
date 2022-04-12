package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.PaypalTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IPaypalTransactionService {

    PaypalTransaction findByPaymentId(String paymentId);

    MessageResponse addPaypalTransaction(String paymentId, CartDTO cartDTO) throws JsonProcessingException;

    void deleteTransaction(String paymentId);
}
