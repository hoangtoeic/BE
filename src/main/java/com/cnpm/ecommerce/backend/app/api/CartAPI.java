package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.OrderStatusDTO;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.enums.PaymentMethod;
import com.cnpm.ecommerce.backend.app.enums.PaypalPaymentIntent;
import com.cnpm.ecommerce.backend.app.enums.PaypalPaymentMethod;
import com.cnpm.ecommerce.backend.app.service.ICartService;
import com.cnpm.ecommerce.backend.app.service.IPaypalTransactionService;
import com.cnpm.ecommerce.backend.app.service.PaypalService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin
public class CartAPI {

    private Logger logger = LoggerFactory.getLogger(CartAPI.class);

    public static final String URL_PAYPAL_SUCCESS ="api/carts/pay/success";
    public static final String URL_PAYPAL_CANCEL ="api/carts/pay/cancel";

    @Autowired
    private ICartService cartService;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private IPaypalTransactionService paypalTransactionService;

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(value = "id", required = false) Long id,
                                              @RequestParam(name = "customerId", required = false) Long customerId,
                                              @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int limit,
                                               @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Cart> cartPage = null;

            if(id == null && customerId == null) {
                cartPage = cartService.findAllPageAndSort(pagingSort);
            } else {
                if(customerId == null) {
                    cartPage = cartService.findByIdContaining(id, pagingSort);
                } else if(id == null) {
                    cartPage = cartService.findByCustomerIdPageAndSort(customerId, pagingSort);
                }

            }

            return new ResponseEntity<>(cartPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }

    // for customer only
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> findAllCartsByCustomer(@PathVariable("customerId") Long customerId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);

            Page<Cart> cartPage = cartService.findByCustomerIdPageAndSort(customerId, pagingSort);

            return new ResponseEntity<>(cartPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = { "/{id}" })
    public ResponseEntity<Cart> getCart(@PathVariable("id") long id) {
        Cart cart = cartService.findById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<?> createCart(@Validated @RequestBody CartDTO cartDto, BindingResult theBindingResult, HttpServletRequest request){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse;

        if(cartDto.getPaymentMethod().equals(PaymentMethod.PAYPAL)){
            try {
                String successUrl = CommonUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
                String cancelUrl = CommonUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;

                Payment payment = paypalService.createPayment(cartDto.getTotalCost().doubleValue(),
                        "USD", PaypalPaymentMethod.PAYPAL,
                        PaypalPaymentIntent.SALE, cartDto.getNote(), cancelUrl, successUrl, cartDto);

                logger.info(payment.toJSON());

                for(Links links : payment.getLinks()) {
                    if(links.getRel().equals("approval_url")) {
                        logger.info(links.getHref());
                        HttpHeaders headers = new HttpHeaders();
                        headers.setLocation(URI.create(links.getHref()));

                        paypalTransactionService.addPaypalTransaction(payment.getId(), cartDto);

                        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

                    }
                }

            } catch(PayPalRESTException | JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        } else {
            messageResponse = cartService.createCart(cartDto);
            return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
        }

        messageResponse = new MessageResponse("Error payment", HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCart(@PathVariable("id") long theId,
                                                      @Validated @RequestBody CartDTO cartDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartService.updateCart(theId, cartDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long theId){

        cartService.deleteCart(theId);
        return new ResponseEntity<>(new MessageResponse("Delete cart successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @PutMapping("/status/{id}")
    public ResponseEntity<MessageResponse> updateStatusCart(@PathVariable("id") long theId,
                                                            @Validated @RequestBody OrderStatusDTO statusDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update product", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartService.updateStatusCart(theId, statusDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @GetMapping(value = { "/pay/success" })
    public ResponseEntity<?> getSuccessPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                CartDTO cartDTO = paypalService.getPayPalPaymentInfo(payment);

                MessageResponse messageResponse = cartService.createCart(cartDTO);

                paypalTransactionService.deleteTransaction(paymentId);

                return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
            }
        } catch (PayPalRESTException | JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(new MessageResponse(
                "Error when processing payment", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = { "/pay/cancel" })
    public ResponseEntity<?> getCancelPayment() {
        return new ResponseEntity<>(new MessageResponse(
                "Payment has been cancel. Please repayment or choose different payment method.", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

}
