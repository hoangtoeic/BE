package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.entity.PaypalTransaction;
import com.cnpm.ecommerce.backend.app.enums.PaypalPaymentIntent;
import com.cnpm.ecommerce.backend.app.enums.PaypalPaymentMethod;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    @Autowired
    private IPaypalTransactionService paypalTransactionService;

    public Payment createPayment(Double total,
                                 String currency,
                                 PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent,
                                 String description,
                                 String cancelUrl,
                                 String successUrl,
                                 CartDTO cartDTO) throws PayPalRESTException, JsonProcessingException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        Double exchange = CommonUtils.exchangeCurrency();
        total = total / exchange;
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        PayerInfo payerInfo = new PayerInfo();
        payer.setPayerInfo(payerInfo);

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        apiContext.setMaskRequestId(true);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public CartDTO getPayPalPaymentInfo(Payment payment) throws JsonProcessingException {

        PaypalTransaction paypalTransaction = paypalTransactionService.findByPaymentId(payment.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        CartDTO cartDTO = objectMapper.readValue(paypalTransaction.getPayload(), CartDTO.class);
        return cartDTO;
    }

}
