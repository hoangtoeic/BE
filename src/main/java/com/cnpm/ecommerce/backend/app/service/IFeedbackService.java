package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.FeedbackDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFeedbackService {

    List<Feedback> findAll();

    Page<Feedback> findAllPageAndSort(Pageable pagingSort);

    Feedback findById(Long theId);

    MessageResponse createFeedback(FeedbackDTO theFeedbackDto);

    MessageResponse updateFeedback(Long theId, FeedbackDTO theFeedbackDto);

    void deleteFeedback(Long theId);

    Page<Feedback> findByRatingContaining(int rating, Pageable pagingSort);

    Long count();
}
