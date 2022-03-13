package com.cnpm.ecommerce.backend.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1086767L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
