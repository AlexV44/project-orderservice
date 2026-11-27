package com.project.order_service.dto.response;

import com.project.order_service.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailedResponse (
        UUID id,
        UUID userId,
        OrderStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        List<OrderItemResponse> items,
        UserResponse user
) {}