package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.FeedbackDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.*;
import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService implements IFeedbackService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Feedback> findAll() {

        return feedbackRepository.findAll();
    }

    @Override
    public Page<Feedback> findAllPageAndSort(Pageable pagingSort) {

        return feedbackRepository.findAll(pagingSort);
    }

    @Override
    public Feedback findById(Long theId) {

        return feedbackRepository.findById(theId).orElseThrow(() -> new ResourceNotFoundException("Not found feedback with ID=" + theId));

    }

    @Override
    public MessageResponse createFeedback(FeedbackDTO theFeedbackDto) {

        // check customerId productId exist
        Optional<User> customer = userRepository.findByIdCustomer(theFeedbackDto.getCustomerId());
        if(!customer.isPresent()) {
            throw new ResourceNotFoundException("Not found customer with ID=" + theFeedbackDto.getCustomerId());
        }

        Optional<Product> product = productRepository.findById(theFeedbackDto.getProductId());
        if(!product.isPresent()) {
            throw new ResourceNotFoundException("Not found product with ID=" + theFeedbackDto.getProductId());
        }

        // check feedback has been existed
        Optional<Feedback> theExistFeedback = feedbackRepository.findByUserIdAndProductId(theFeedbackDto.getCustomerId(), theFeedbackDto.getProductId());

        if(theExistFeedback.isPresent()) {
            return new MessageResponse("Can not rating, because you was rated it, just only update!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        // find all carts for customerId
        List<Cart> carts = cartRepo.findByCustomer(theFeedbackDto.getCustomerId());

        for(Cart cart : carts) {
            for(CartItem cartItem : cart.getCartItems()) {
                if(cartItem.getProduct().getId() == theFeedbackDto.getProductId()) {
                    if(cart.getStatus().equals(OrderStatus.COMPLETED)) {
                        Feedback theFeedback = new Feedback();

                        theFeedback.setRating(theFeedbackDto.getRating());
                        theFeedback.setProduct(product.get());
                        theFeedback.setCreatedDate(new Date());
                        theFeedback.setCreatedBy(customer.get().getUserName());
                        theFeedback.setUser(customer.get());
                        feedbackRepository.save(theFeedback);

                        // update rating average for product
                        updateRatingAverage(theFeedbackDto, product);

                        return new MessageResponse("Create feedback successfully!", HttpStatus.CREATED, LocalDateTime.now());
                    }
                }
            }
        }


        return new MessageResponse("Can not rating, because " +
                "your order with this product hasn't completed or " +
                "you haven't buy this product yet", HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateFeedback(Long theId, FeedbackDTO theFeedbackDto) {
        Optional<Feedback> theFeedback = feedbackRepository.findById(theId);

        if (!theFeedback.isPresent()) {
            throw new ResourceNotFoundException("Not found feedback with ID=" + theId);
        } else {
            // check customerId productId exist
            Optional<User> customer = userRepository.findByIdCustomer(theFeedbackDto.getCustomerId());
            if(!customer.isPresent()) {
                throw new ResourceNotFoundException("Not found customer with ID=" + theFeedbackDto.getCustomerId());
            }

            Optional<Product> product = productRepository.findById(theFeedbackDto.getProductId());
            if(!product.isPresent()) {
                throw new ResourceNotFoundException("Not found product with ID=" + theFeedbackDto.getProductId());
            }

            // check feedback has been existed
            Optional<Feedback> theExistFeedback = feedbackRepository.findByUserIdAndProductId(theFeedbackDto.getCustomerId(), theFeedbackDto.getProductId());

            if(!theExistFeedback.isPresent()) {
                return new MessageResponse("Can not update rating, because you haven't been rated it!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }

            // find all carts for customerId
            List<Cart> carts = cartRepo.findByCustomer(theFeedbackDto.getCustomerId());

            for(Cart cart : carts) {
                for(CartItem cartItem : cart.getCartItems()) {
                    if(cartItem.getProduct().getId() == theFeedbackDto.getProductId()) {
                        if(cart.getStatus().equals(OrderStatus.COMPLETED)) {

                            theExistFeedback.get().setRating(theFeedbackDto.getRating());
                            theExistFeedback.get().setModifiedDate(new Date());
                            theExistFeedback.get().setModifiedBy(customer.get().getUserName());
                            feedbackRepository.save(theExistFeedback.get());

                            // update rating average for product
                            updateRatingAverage(theFeedbackDto, product);

                            return new MessageResponse("Update feedback successfully!", HttpStatus.OK, LocalDateTime.now());
                        }
                    }
                }
            }


            return new MessageResponse("Can not rating, because " +
                    "your order with this product hasn't completed or " +
                    "you haven't buy this product yet", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
    }

    private void updateRatingAverage(FeedbackDTO theFeedbackDto, Optional<Product> product) {
        long sum = 0 ;
        List<Feedback> feedbacks = feedbackRepository.findByProductId(theFeedbackDto.getProductId());
        if(feedbacks.size() != 0) {
            for (Feedback feedback: feedbacks) {
                sum += feedback.getRating();
            }
            final DecimalFormat df = new DecimalFormat("0.00");
            float average =  (float) sum/ (float) feedbacks.size();
            BigDecimal convertAverage = new BigDecimal (df.format(average));
            product.get().setRatingAverage(convertAverage);

            productRepository.save(product.get());
        }
    }

    @Override
    public void deleteFeedback(Long theId) {

        Feedback theFeedback = feedbackRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found feedback with ID=" + theId));

        feedbackRepository.delete(theFeedback);

    }

    @Override
    public Long count() {

        return feedbackRepository.count();
    }

    @Override
    public Page<Feedback> findByRatingContaining(int rating, Pageable pagingSort) {
        return feedbackRepository.findByRating(rating, pagingSort);
    }
}