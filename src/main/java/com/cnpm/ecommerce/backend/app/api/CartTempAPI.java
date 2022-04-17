package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CartTempDTO;
import com.cnpm.ecommerce.backend.app.dto.DeleteDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartTemp;
import com.cnpm.ecommerce.backend.app.service.ICartTempService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cartTemps")
@CrossOrigin
public class CartTempAPI {

    @Autowired
    private ICartTempService cartTempService;

    @GetMapping()
    public ResponseEntity<?> findAll( @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int limit,
                                      @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<CartTemp> cartPage = null;

                cartPage = cartTempService.findAllPageAndSort(pagingSort);

            return new ResponseEntity<>(cartPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }

    // for customer only
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> findAllCartTempsByCustomer(@PathVariable("customerId") Long customerId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);

            Page<CartTemp> cartPage = cartTempService.findByCustomerIdPageAndSort(customerId, pagingSort);

            return new ResponseEntity<>(cartPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = { "/{id}" })
    public ResponseEntity<?> getCart(@PathVariable("id") Long id) {
        CartTemp cart = cartTempService.findById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<?> createCart(@Validated @RequestBody CartTempDTO cartDto, BindingResult theBindingResult, HttpServletRequest request){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartTempService.createCartTemp(cartDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());

    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCart(@PathVariable("id") long theId,
                                                      @Validated @RequestBody CartTempDTO cartDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for update cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartTempService.updateCartTemp(theId, cartDto);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartTemp(@PathVariable("id") long theId){

        MessageResponse messageResponse = cartTempService.deleteCart(theId);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping("/deletes")
    public ResponseEntity<?> deleteMultipleCartTemp(@RequestBody DeleteDTO ids){

        List<Long> theIds = ids.getIds();

        MessageResponse messageResponse = cartTempService.deleteMultipleCart(theIds);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }



}
