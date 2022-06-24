package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CommentDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Comment;
import com.cnpm.ecommerce.backend.app.service.ICommentService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentAPI {
    @Autowired
    private ICommentService commentService;

    @GetMapping("")
    public ResponseEntity<Page<Comment>> findAll(@RequestParam(name = "productId", required = false) Long productId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int limit,
                                                 @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Comment> commentPage;

            if(productId == null) {
                commentPage = commentService.findAllPageAndSortComment(pagingSort);
            } else {
                commentPage = commentService.findByProductIdPageAndSort(productId, pagingSort);
            }

             //   commentPage = commentService.findAllPageAndSortComment(pagingSort);
            return new ResponseEntity<>(commentPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<MessageResponse> createComment(@Valid @RequestBody CommentDTO theCommentDTO, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create feedback", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = commentService.createComment(theCommentDTO);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }



}
