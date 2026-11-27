package com.project.order_service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequest (
        @NotNull(message = "Item id cannot be null")
        UUID itemId,

        @NotNull(message = "Quantity cannot be null")
        Integer quantity
) {}
