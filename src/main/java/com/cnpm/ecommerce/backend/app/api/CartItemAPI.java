package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CartItemDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import com.cnpm.ecommerce.backend.app.service.ICartItemService;
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
@RequestMapping("/api/cartItems")
@CrossOrigin
public class CartItemAPI {
    @Autowired
    private ICartItemService cartItemService;

    @GetMapping(value = { "", "/" })
    public ResponseEntity<List<CartItem>> findAll(@RequestParam(value = "id", required = false) Long id,
                                                  @RequestParam(value = "cartId", required = false) Long cartId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int limit,
                                                  @RequestParam(defaultValue = "id,ASC") String[] sort){
        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<CartItem> cartItemPage = null;

            if (id == null) {
                cartItemPage = cartItemService.findAllPageAndSort(pagingSort);
            } else {
                cartItemPage = cartItemService.findByIdContaining(id, pagingSort);
            }
            if(id == null && cartId == null) {
                cartItemPage = cartItemService.findAllPageAndSort(pagingSort);
            } else {
                if(cartId == null) {
                    cartItemPage = cartItemService.findByIdContaining(id, pagingSort);
                } else if(id == null) {
                    cartItemPage = cartItemService.findByCartIdPageAndSort(cartId, pagingSort);
                }

            }


            return new ResponseEntity<>(cartItemPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = { "/{cid}" })
    public ResponseEntity<CartItem> getCartItem(@PathVariable("cid") long id) {
        CartItem cartItem = cartItemService.findById(id);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createCartItem(@Valid @RequestBody CartItemDTO cartItemDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid param for creating cart item", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartItemService.createCartItem(cartItemDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCartItem(@PathVariable("id") long theId,
                                                          @Valid @RequestBody CartItemDTO cartItemDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid param for modifying cart item", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = cartItemService.updateCartItem(theId, cartItemDto);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") long theId){

        cartItemService.deleteCartItem(theId);
        return new ResponseEntity<>(new MessageResponse("Delete cart item successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> findCartsByCartId(@PathVariable("cartId") long cartId ){

        List<CartItem> cartItems = cartItemService.findByCartId(cartId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);

    }
}
