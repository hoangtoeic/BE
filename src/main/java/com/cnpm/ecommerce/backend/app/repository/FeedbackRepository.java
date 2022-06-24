package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByRating(int rating, Pageable pageable);

    Optional<Feedback> findByUserIdAndProductId(Long customerId, Long productId);

    List<Feedback> findByProductId(Long productId);


    @Query("SELECT f FROM Feedback f WHERE f.user.id = ?1")
    List<Feedback> findProductBuyMostRecent(Long user_id);

    @Query("select f.product.id from Feedback f WHERE f.rating < 4 AND f.user.id = ?1")
    List<Long> findProductIDRatingSmaller4(Long user_id);
}

