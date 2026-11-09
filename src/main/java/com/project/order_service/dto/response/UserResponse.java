package com.project.order_service.dto.response;

import java.util.UUID;

public record UserResponse (
        UUID id,
        String email,
        String name,
        String surname
) {}
