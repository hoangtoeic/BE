package com.cnpm.ecommerce.backend.app.service;


import com.cnpm.ecommerce.backend.app.dto.CommentDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Comment;
import com.cnpm.ecommerce.backend.app.entity.Product;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional
public class CommentService implements ICommentService {

    @Autowired
    private IProductService productService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IUserService customerService;


    @Override
    public MessageResponse createComment(CommentDTO theCommentDTO) {

        Comment theComment = new Comment();

        theComment.setComment((theCommentDTO.getComment()));
        theComment.setUser(customerService.findByIdCustomer(theCommentDTO.getCustomerId()));
        theComment.setProduct(productService.findById(theCommentDTO.getProductId()));
        theComment.setCreatedDate(new Date());
        theComment.setCreatedBy("");

        commentRepository.save((theComment));
        return new MessageResponse("Create comment successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public Page<Comment> findAllPageAndSortComment(Pageable pagingSort) {
        return commentRepository.findAll(pagingSort);
    }

    @Override
    public Page<Comment> findByProductIdPageAndSort(Long productId, Pageable pagingSort) {
        Product product = productService.findById(productId);
        Page<Comment> commentPage = commentRepository.findByProductId(productId, pagingSort);
        if (product == null) {
            throw new ResourceNotFoundException("Not found product with ID= " + productId);
        } else {


            for(Comment comment : commentPage.getContent()) {
                comment.setProduct_id(comment.getProduct().getId());
                comment.setUser_id(comment.getUser().getId());
            }
            return  commentPage;
        }


    }
}