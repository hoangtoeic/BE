package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.service.ICartService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin
public class CartAPI {

    @Autowired
    private ICartService cartService;

    @GetMapping(value = { "", "/" })
    public ResponseEntity<List<Cart>> findAll(@RequestParam(value = "id", required = false) Long id,
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

            return new ResponseEntity<>(cartPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = { "/{cid}" })
    public ResponseEntity<Cart> getCart(@PathVariable("cid") long id) {
        Cart cart = cartService.findById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createCart(@Valid @RequestBody CartDTO cartDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartService.createCart(cartDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCart(@PathVariable("id") long theId,
                                                      @Valid @RequestBody CartDTO cartDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update product", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartService.updateCart(theId, cartDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long theId){

        cartService.deleteCart(theId);
        return new ResponseEntity<>(new MessageResponse("Delete cart successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Cart>> findCartsByCustomerId(@PathVariable("customerId") long customerId ){

        List<Cart> carts = cartService.findByCustomerId(customerId);
        return new ResponseEntity<>(carts, HttpStatus.OK);

    }

}
