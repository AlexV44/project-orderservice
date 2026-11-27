package com.project.order_service.exception.notfound;

import java.util.UUID;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(UUID id) {
        super("Item with id " + id + " not found");
    }
}