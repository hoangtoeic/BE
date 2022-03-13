package com.cnpm.ecommerce.backend.app.exception;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){

        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        MessageResponse messageResponse = new MessageResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }
}
