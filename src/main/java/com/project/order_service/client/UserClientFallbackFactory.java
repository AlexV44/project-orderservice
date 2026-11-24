package com.project.order_service.client;

import com.project.order_service.dto.response.UserResponse;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.UUID;

public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public UserResponse getUserById(UUID userId) {
                rethrowClientError(cause);
                return new UserResponse(userId, "service-unavailable@system", "Unknown", "User");
            }

            @Override
            public UserResponse getUserByEmail(String email) {
                rethrowClientError(cause);
                return new UserResponse(null, email, "Unknown", "User");
            }
        };
    }

    private void rethrowClientError(Throwable cause) {
        if (cause instanceof FeignException fe && fe.status() >= 400 && fe.status() < 500) {
            throw fe;
        }
    }
}
