package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CommentDTO;
import com.cnpm.ecommerce.backend.app.dto.FeedbackDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Comment;
import com.cnpm.ecommerce.backend.app.entity.Feedback;
import com.cnpm.ecommerce.backend.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
  //  List<Comment> findAll();

  //  Page<Comment> findAllPageAndSort(Pageable pagingSort);

 //   Comment findById(Long theId);

    MessageResponse createComment(CommentDTO theCommentDTO);

    Page<Comment> findAllPageAndSortComment(Pageable pagingSort);

    Page<Comment> findByProductIdPageAndSort(Long productId, Pageable pagingSort);

//    MessageResponse updateComment(Long theId, CommentDTO theCommentDTO);

//    void deleteComment(Long theId);

 //   Long count();

 //   Long countCommentByProductId(Long theProductId);
}
