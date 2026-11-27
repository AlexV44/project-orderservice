package com.project.order_service.client;

import com.project.order_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "${app.services.user-service.url}",
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    UserResponse getUserById(@PathVariable("id") UUID userId);

    @GetMapping("/api/v1/users/search-by-email")
    UserResponse getUserByEmail(@RequestParam("email") String email);
}