package com.project.order_service.client;

import com.project.order_service.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserResponse getUserById(UUID userId) {
        return new UserResponse(
                userId,
                "service-unavailable@system",
                "Unknown",
                "User"
        );
    }
}
