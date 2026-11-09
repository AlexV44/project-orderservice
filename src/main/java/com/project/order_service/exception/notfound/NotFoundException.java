package com.project.order_service.exception.notfound;

import com.project.order_service.exception.BaseHttpException;
import org.springframework.http.HttpStatus;
import tools.jackson.core.ObjectReadContext;

public class NotFoundException extends BaseHttpException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
