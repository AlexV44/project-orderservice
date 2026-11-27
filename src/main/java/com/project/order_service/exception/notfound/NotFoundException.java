package com.project.order_service.exception.notfound;

import com.project.order_service.exception.BaseHttpException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseHttpException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
