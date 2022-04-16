package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.FeedbackDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import com.cnpm.ecommerce.backend.app.entity.Feedback;
import com.cnpm.ecommerce.backend.app.enums.OrderStatus;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.CartItemRepository;
import com.cnpm.ecommerce.backend.app.repository.CartRepository;
import com.cnpm.ecommerce.backend.app.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService implements IFeedbackService {
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService customerService;

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
        Feedback theFeedback = new Feedback();
        // find CardId by Status and UserId
        List<Cart> cartList = cartRepo.findAll();

        List<CartItem> cartItem = cartItemRepo.findAll();
        // find all feedBack to check same userId and productId although different cartId
        List<Feedback> feedbacks = feedbackRepository.findAll();
        int checkExistUserProductInFeedBacks = 0;
        for (Feedback feedback1: feedbacks) {
            if(feedback1.getProduct().getId() == theFeedbackDto.getProductId()
                    && feedback1.getUser().getId() == theFeedbackDto.getCustomerId() )
                checkExistUserProductInFeedBacks = 1;
        }
        if(checkExistUserProductInFeedBacks == 1)
            throw new ResourceNotFoundException("Can not rating, because you was rated it");
        // check user buy product and userID exists

        LinkedList<Integer> linkedlist=new LinkedList<Integer>();

        int  checkStatus = 0;
        for(Cart cart : cartList) {

            if(cart.getUser().getId() == theFeedbackDto.getCustomerId() && cart.getStatus() == OrderStatus.COMPLETED) {
                linkedlist.add(Math.toIntExact(cart.getId()));
            }
        }

        if ( linkedlist.size() == 0)
            throw new ResourceNotFoundException("Can not rating, because " +
                    "your order with this product hasn't completed or " +
                    "you haven't buy this product yet");
        int checkUserExist = 0;
        int checkUserBuyProduct = 0;
        int checkBeforeCreate = 0;
        for(Integer ls: linkedlist) {
            System.out.println("linkedlist "  + ls);
            for(CartItem cartItem2 : cartItem) {
                long productId = cartItem2.getProduct().getId();
           //     System.out.println("productId " + productId );
                long convertToInteger = Integer.parseInt(ls.toString());
               // long convertToInteger = (cartItem2.getCartIds()).intValue();
            //    System.out.println("convertToInteger " + convertToInteger );
            //    System.out.println("ls " + ls );

                if(convertToInteger == cartItem2.getCart().getId() )
                {
                    if(productId == theFeedbackDto.getProductId() ) {
                        checkUserBuyProduct = 1;
                    }
                }
            }
        }

        if( checkUserBuyProduct == 1) {
            theFeedback.setRating(theFeedbackDto.getRating());
            theFeedback.setProduct(productService.findById(theFeedbackDto.getProductId()));
            theFeedback.setCreatedDate(new Date());
            theFeedback.setCreatedBy("");
            theFeedback.setUser(customerService.findByIdCustomer(theFeedbackDto.getCustomerId()));
            feedbackRepository.save(theFeedback);

            return new MessageResponse("Create feedback successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }
        else  {
            throw new ResourceNotFoundException("Can not rating, because " +
                    "your order with this product hasn't completed or " +
                    "you haven't buy this product yet");
        }

    }

    @Override
    public MessageResponse updateFeedback(Long theId, FeedbackDTO theFeedbackDto) {
        Optional<Feedback> theFeedback = feedbackRepository.findById(theId);

        if (!theFeedback.isPresent()) {
            throw new ResourceNotFoundException("Not found product with ID=" + theId);
        } else {
            theFeedback.get().setRating(theFeedbackDto.getRating());
            theFeedback.get().setProduct(productService.findById(theFeedbackDto.getProductId()));
            theFeedback.get().setCreatedDate(new Date());
            theFeedback.get().setCreatedBy("");

            feedbackRepository.save(theFeedback.get());
        }

        return new MessageResponse("Update feedback successfully!", HttpStatus.OK, LocalDateTime.now());
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
        return feedbackRepository.findByRatingContaining(rating, pagingSort);
    }
}