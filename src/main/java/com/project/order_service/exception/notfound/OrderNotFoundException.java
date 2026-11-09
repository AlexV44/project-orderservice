package com.project.order_service.exception.notfound;

import java.util.UUID;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(UUID id) {
        super("Order with id " + id + " not found");
    }
}
