package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.LogoutRequest;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Blacklist;
import com.cnpm.ecommerce.backend.app.entity.PaypalTransaction;
import com.cnpm.ecommerce.backend.app.entity.RefreshToken;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.BlacklistRepository;
import com.cnpm.ecommerce.backend.app.repository.PaypalTransactionRepository;
import com.cnpm.ecommerce.backend.app.repository.RefreshTokenRepository;
import com.cnpm.ecommerce.backend.app.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class PaypalTransactionService implements IPaypalTransactionService {

    @Autowired
    private PaypalTransactionRepository paypalTransactionRepository;

    @Override
    public PaypalTransaction findByPaymentId(String paymentId) throws ResourceNotFoundException {
        Optional<PaypalTransaction> paypalTransaction = paypalTransactionRepository.findByPaymentId(paymentId);
        if(!paypalTransaction.isPresent()) {
            throw new ResourceNotFoundException("Not found payment transaction");
        }
        return paypalTransaction.get();
    }

    @Override
    public MessageResponse addPaypalTransaction(String paymentId, CartDTO cartDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PaypalTransaction paypalTransaction = new PaypalTransaction();
        paypalTransaction.setPaymentId(paymentId);
        paypalTransaction.setCreatedDate(new Date());
        paypalTransaction.setPayload(objectMapper.writeValueAsString(cartDTO));

        paypalTransactionRepository.save(paypalTransaction);

        return new MessageResponse("Add payment transaction successfuly!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteTransaction(String paymentId) {
        Optional<PaypalTransaction> paypalTransaction = paypalTransactionRepository.findByPaymentId(paymentId);
        if(!paypalTransaction.isPresent()) {
            throw new ResourceNotFoundException("Not found payment transaction");
        } else {
            paypalTransactionRepository.delete(paypalTransaction.get());
        }
    }
}
