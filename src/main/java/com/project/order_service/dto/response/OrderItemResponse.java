package com.project.order_service.dto.response;

import java.util.UUID;

public record OrderItemResponse (
        UUID id,
        UUID itemId,
        Integer quantity
) {}
