package com.project.order_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseHttpException extends RuntimeException {
    private final HttpStatus status;

    public BaseHttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
