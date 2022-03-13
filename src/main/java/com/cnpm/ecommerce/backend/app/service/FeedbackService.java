package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.FeedbackDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Feedback;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.FeedbackRepository;
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
public class FeedbackService implements IFeedbackService {

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

        theFeedback.setRating(theFeedbackDto.getRating());
        theFeedback.setProduct(productService.findById(theFeedbackDto.getProductId()));
        theFeedback.setCreatedDate(new Date());
        theFeedback.setCreatedBy("");
        theFeedback.setUser(customerService.findByIdCustomer(theFeedbackDto.getCustomerId()));
        feedbackRepository.save(theFeedback);

        return new MessageResponse("Create feedback successfully!", HttpStatus.CREATED, LocalDateTime.now());
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