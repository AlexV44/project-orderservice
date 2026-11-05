package com.project.order_service.dto.request;

import com.project.order_service.entity.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderUpdateRequest (
        @NotNull(message = "Order status cannot be null")
        OrderStatus status,

        @NotNull(message = "Total price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than zero")
        BigDecimal totalPrice,

        @Valid
        @NotNull(message = "Items list cannot be null")
        @NotEmpty(message = "Order must contain at least one item")
        List<OrderItemRequest> items
) {}
