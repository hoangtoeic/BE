package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartDTO;
import com.cnpm.ecommerce.backend.app.dto.CartItemDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.OrderStatusDTO;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import com.cnpm.ecommerce.backend.app.entity.Product;
import com.cnpm.ecommerce.backend.app.entity.User;
import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.enums.PaymentMethod;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService implements ICartService{

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;

    @Override
    public List<Cart> findAll() {
        return cartRepo.findAll();
    }

    @Override
    public Page<Cart> findAllPageAndSort(Pageable pagingSort) {

        Page<Cart> cartPage = cartRepo.findAll(pagingSort);

        for(Cart cart : cartPage.getContent()) {
            cart.setUserIds(cart.getUser().getId());
            for(CartItem cartItem : cart.getCartItems()) {
                cartItem.setCartIds(cart.getId());
                cartItem.setProductIds(cartItem.getProduct().getId());
            }
        }
        return  cartPage;
    }

    @Override
    public Cart findById(Long theId) {

        Optional<Cart> cart = cartRepo.findById(theId);
        if(!cart.isPresent()) {
            throw  new ResourceNotFoundException("Not found cart with ID=" + theId);
        } else {
            cart.get().setUserIds(cart.get().getUser().getId());
            for(CartItem cartItem : cart.get().getCartItems()) {
                cartItem.setCartIds(cart.get().getId());
                cartItem.setProductIds(cartItem.getProduct().getId());
            }
            return cart.get();
        }

    }


    @Override
    public List<Cart> findByCustomerId(long customerId) {
        return cartRepo.findByCustomer(customerId);
    }

    @Override
    public MessageResponse createCart(CartDTO cartDTO) {

        if(cartDTO.getPaymentMethod().equals(PaymentMethod.CASH)) {
            Cart cart = new Cart();
            cart.setTotalCost(cartDTO.getTotalCost());
            cart.setNote(cartDTO.getNote());
            Optional<User> customer = userRepo.findByIdCustomer(cartDTO.getCustomerId());
            if(!customer.isPresent()) {
                throw  new ResourceNotFoundException("Not found customer with ID=" + cartDTO.getCustomerId());
            }
            cart.setUser(customer.get());
            cart.setAddress(cartDTO.getAddress());
            cart.setPaymentMethod(PaymentMethod.CASH);
            cart.setStatus(cartDTO.getStatus() == null ? OrderStatus.PENDING : cartDTO.getStatus());
            cart.setCreatedBy(cartDTO.getCreatedBy());
            cart.setCreatedDate(new Date());

            Cart savedCart = cartRepo.save(cart);

            for( CartItemDTO cartItemDTO : cartDTO.getCartItems()){
                CartItem cartItem = new CartItem();
                cartItem.setCart(savedCart);
                Optional<Product> product = productRepo.findById(cartItemDTO.getProductId());
                if(!product.isPresent()) {
                    throw  new ResourceNotFoundException("Not found product with ID=" + cartItemDTO.getProductId());
                }
                cartItem.setProduct(product.get());
                cartItem.setQuantity(cartItemDTO.getQuantity() == null ? 1 : cartItemDTO.getQuantity());
                cartItem.setSalePrice(cartItemDTO.getSalePrice());
                cartItem.setCreatedBy(cartItemDTO.getCreatedBy());
                cartItem.setCreatedDate(new Date());

                cartItemRepo.save(cartItem);
            }

            return new MessageResponse("Create cart successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }

        return null;

    }

    @Override
    public MessageResponse updateCart(Long theId, CartDTO cartDTO) {

        Optional<Cart> cart = cartRepo.findById(theId);

        if(!cart.isPresent()) {
            throw new ResourceNotFoundException("Can't find Cart with ID=" + theId);
        } else {
            cart.get().setTotalCost(cartDTO.getTotalCost());
            cart.get().setNote(cartDTO.getNote());
            Optional<User> customer = userRepo.findByIdCustomer(cartDTO.getCustomerId());
            if(!customer.isPresent()) {
                throw  new ResourceNotFoundException("Not found customer with ID=" + cartDTO.getCustomerId());
            }
            cart.get().setUser(customer.get());
            cart.get().setAddress(cartDTO.getAddress());
            cart.get().setStatus(cartDTO.getStatus());
            cart.get().setModifiedBy(cartDTO.getModifiedBy());
            cart.get().setModifiedDate(cartDTO.getModifiedDate());

            cartRepo.save(cart.get());

            for(CartItemDTO cartItemDTO : cartDTO.getCartItems()){
                Optional<CartItem> cartItem = cartItemRepo.findById(cartItemDTO.getId());
                if(!cartItem.isPresent()) {
                    throw  new ResourceNotFoundException("Not found cartItem with ID=" + cartItemDTO.getId());
                }
                Optional<Product> product = productRepo.findById(cartItemDTO.getProductId());
                if(!product.isPresent()) {
                    throw  new ResourceNotFoundException("Not found product with ID=" + cartItemDTO.getProductId());
                }
                cartItem.get().setProduct(product.get());
                cartItem.get().setQuantity(cartItemDTO.getQuantity() == null ? 1 : cartItemDTO.getQuantity());
                cartItem.get().setSalePrice(cartItemDTO.getSalePrice());
                cartItem.get().setModifiedBy(cartItemDTO.getModifiedBy());
                cartItem.get().setModifiedDate(new Date());

                cartItemRepo.save(cartItem.get());
            }

        }

        return new MessageResponse("Update cart successfully!" , HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteCart(Long theId) {
        Cart cart = cartRepo.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("can't find cart with ID=" + theId));

        cartRepo.delete(cart);
    }

    @Override
    public Page<Cart> findByIdContaining(Long id, Pageable pagingSort) {

        Page<Cart> cartPage = cartRepo.findById(id, pagingSort);

        for(Cart cart : cartPage.getContent()) {
            cart.setUserIds(cart.getUser().getId());
            for(CartItem cartItem : cart.getCartItems()) {
                cartItem.setCartIds(cart.getId());
                cartItem.setProductIds(cartItem.getProduct().getId());
            }
        }
        return  cartPage;
    }

    @Override
    public Page<Cart> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort) throws ResourceNotFoundException {
            Optional<User> customer = userRepo.findByIdCustomer(customerId);

            if(!customer.isPresent()){
                throw  new ResourceNotFoundException("Not found customer with ID= " + customerId);
            } else {
                Page<Cart> cartPage = cartRepo.findByUserId(customerId,pagingSort);

                for(Cart cart : cartPage.getContent()) {
                    cart.setUserIds(cart.getUser().getId());
                    for(CartItem cartItem : cart.getCartItems()) {
                        cartItem.setCartIds(cart.getId());
                        cartItem.setProductIds(cartItem.getProduct().getId());
                    }
                }
                return  cartPage;
            }


    }

    @Override
    public MessageResponse updateStatusCart(long theId, OrderStatusDTO statusDto) {

        Optional<Cart> cart = cartRepo.findById(theId);

        if(!cart.isPresent()) {
            throw  new ResourceNotFoundException("Not found cart with ID= " + theId);
        } else {
            Optional<User> user = userRepo.findById(statusDto.getUserId());
            if(!user.isPresent()) {
                throw  new ResourceNotFoundException("Not found user with ID= " + statusDto.getUserId());
            } else {
                if(cart.get().getStatus().equals(OrderStatus.COMPLETED)) {
                    return new MessageResponse("Order has been completed, please don't change status.", HttpStatus.OK, LocalDateTime.now());
                } else {
                    cart.get().setStatus(statusDto.getStatus());
                    cart.get().setModifiedBy(user.get().getUserName());
                    cart.get().setModifiedDate(new Date());
                    cartRepo.save(cart.get());

                    if(statusDto.getStatus().equals(OrderStatus.COMPLETED)) {
                        List<CartItem> cartItems = cart.get().getCartItems();

                        for(CartItem cartItem : cartItems) {
                            Optional<Product> product = productRepo.findById(cartItem.getProduct().getId());

                            product.get().setUnitInStock(product.get().getUnitInStock() - cartItem.getQuantity());
                            product.get().setModifiedBy(user.get().getUserName());
                            product.get().setModifiedDate(new Date());

                            productRepo.save(product.get());
                        }
                    }
                }
            }
        }
        return new MessageResponse("Update order status successfully.", HttpStatus.OK, LocalDateTime.now());
    }


}
