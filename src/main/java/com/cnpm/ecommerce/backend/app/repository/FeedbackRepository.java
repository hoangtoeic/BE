package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByRating(int rating, Pageable pageable);

    Optional<Feedback> findByUserIdAndProductId(Long customerId, Long productId);

    List<Feedback> findByProductId(Long productId);
}
