package com.project.order_service.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemResponse(
        UUID id,
        String name,
        BigDecimal price
) {}
