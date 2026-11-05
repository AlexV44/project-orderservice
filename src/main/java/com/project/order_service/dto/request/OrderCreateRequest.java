package com.project.order_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderCreateRequest (
        @NotNull(message = "User id cannot be null")
        UUID userId,

        @Valid
        @NotNull(message = "Items list cannot be null")
        @NotEmpty(message = "Order must contain at least one item")
        List<OrderItemRequest> items
) {}
