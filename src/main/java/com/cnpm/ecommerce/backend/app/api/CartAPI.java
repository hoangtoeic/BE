package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.OrderStatusDTO;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.service.ICartService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin
public class CartAPI {

    @Autowired
    private ICartService cartService;

    @GetMapping(value = { "", "/" })
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

    @GetMapping(value = { "/{id}" })
    public ResponseEntity<Cart> getCart(@PathVariable("id") long id) {
        Cart cart = cartService.findById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createCart(@Validated @RequestBody CartDTO cartDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartService.createCart(cartDto);
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

}
