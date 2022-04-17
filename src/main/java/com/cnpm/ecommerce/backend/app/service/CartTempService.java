package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CartTempDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.CartTemp;
import com.cnpm.ecommerce.backend.app.entity.Product;
import com.cnpm.ecommerce.backend.app.entity.User;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.CartTempRepository;
import com.cnpm.ecommerce.backend.app.repository.ProductRepository;
import com.cnpm.ecommerce.backend.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartTempService implements ICartTempService{

    @Autowired
    private CartTempRepository cartTempRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;


    @Override
    public Page<CartTemp> findAllPageAndSort(Pageable pagingSort) {
        Page<CartTemp> cartPage = cartTempRepository.findAll(pagingSort);

        for(CartTemp cart : cartPage.getContent()) {
            cart.setCustomerIds(cart.getCustomer().getId());
            cart.setProductIds(cart.getProduct().getId());

        }
        return  cartPage;
    }

    @Override
    public CartTemp findById(Long theId) {
        Optional<CartTemp> cart = cartTempRepository.findById(theId);
        if(!cart.isPresent()) {
            throw  new ResourceNotFoundException("Not found cart temp with ID=" + theId);
        } else {
            cart.get().setCustomerIds(cart.get().getCustomer().getId());
            cart.get().setProductIds(cart.get().getProduct().getId());
        }
        return cart.get();
    }

    @Override
    public MessageResponse createCartTemp(CartTempDTO cartTempDTO) {

        Optional<CartTemp> cart = cartTempRepository.findByCustomerIdAndProductId(cartTempDTO.getCustomerId(), cartTempDTO.getProductId());

        if(cart.isPresent()) {
            cart.get().setQuantity(cart.get().getQuantity() + cartTempDTO.getQuantity());
            cart.get().setSalePrice(cartTempDTO.getSalePrice());
            cart.get().setModifiedDate(new Date());
            cart.get().setModifiedBy(userRepo.findByIdCustomer(cartTempDTO.getCustomerId()).get().getUserName());

            cartTempRepository.save(cart.get());
        } else {
            Optional<User> customer = userRepo.findByIdCustomer(cartTempDTO.getCustomerId());
            if(!customer.isPresent()) {
                throw new ResourceNotFoundException("Not found customer with ID=" + cartTempDTO.getCustomerId());
            }

            Optional<Product> product = productRepo.findById(cartTempDTO.getProductId());
            if(!product.isPresent()) {
                throw new ResourceNotFoundException("Not found product with ID=" + cartTempDTO.getProductId());
            }

            CartTemp cartTemp = new CartTemp();
            cartTemp.setCustomer(customer.get());
            cartTemp.setProduct(product.get());
            cartTemp.setQuantity(cartTempDTO.getQuantity());
            cartTemp.setSalePrice(cartTempDTO.getSalePrice());
            cartTemp.setCreatedBy(customer.get().getUserName());
            cartTemp.setCreatedDate(new Date());

            cartTempRepository.save(cartTemp);

        }

        return new MessageResponse("Create cart temp successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateCartTemp(Long theId, CartTempDTO cartTempDTO) {

        Optional<CartTemp> cart = cartTempRepository.findById(theId);

        if(cart.isPresent()) {
            if(cart.get().getCustomer().getId() != cartTempDTO.getCustomerId()) {
                return new MessageResponse("Customer id is diff!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }

            if(cart.get().getProduct().getId() != cartTempDTO.getProductId()) {
                return new MessageResponse("Product id is diff!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }
            cart.get().setQuantity(cartTempDTO.getQuantity());
            cart.get().setSalePrice(cartTempDTO.getSalePrice());
            cart.get().setModifiedDate(new Date());
            cart.get().setModifiedBy(userRepo.findByIdCustomer(cartTempDTO.getCustomerId()).get().getUserName());

            cartTempRepository.save(cart.get());

            return new MessageResponse("Update cart temp successfully!" , HttpStatus.OK, LocalDateTime.now());
        }
        return new MessageResponse("Not found cart temp with ID=" + theId, HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @Override
    public MessageResponse deleteCart(Long theId) {
        CartTemp cart = cartTempRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("can't find cart temp with ID=" + theId));

        cartTempRepository.delete(cart);

        return new MessageResponse("Delete cart temp successfully!" , HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public MessageResponse deleteMultipleCart(List<Long>  theIds) {
        for(Long theId : theIds) {
            CartTemp cart = cartTempRepository.findById(theId).orElseThrow(
                    () -> new ResourceNotFoundException("can't find cart temp with ID=" + theId));
        }

        for(Long theId : theIds) {
            CartTemp cart = cartTempRepository.findById(theId).orElseThrow(
                    () -> new ResourceNotFoundException("can't find cart temp with ID=" + theId));

            cartTempRepository.delete(cart);
        }
        return new MessageResponse("Delete all cart temp successfully!" , HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public Page<CartTemp> findByCustomerIdPageAndSort(Long customerId, Pageable pagingSort) {
        Page<CartTemp> cartPage = cartTempRepository.findByCustomerId(customerId, pagingSort);

        for(CartTemp cart : cartPage.getContent()) {
            cart.setCustomerIds(cart.getCustomer().getId());
            cart.setProductIds(cart.getProduct().getId());

        }
        return  cartPage;
    }
}
