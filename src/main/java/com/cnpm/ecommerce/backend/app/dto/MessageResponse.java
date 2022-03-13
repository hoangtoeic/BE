package com.cnpm.ecommerce.backend.app.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class MessageResponse {

    private String message;

    private HttpStatus status;

    private LocalDateTime timestamp;

    public MessageResponse() {
    }

    public MessageResponse(String message, HttpStatus status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
