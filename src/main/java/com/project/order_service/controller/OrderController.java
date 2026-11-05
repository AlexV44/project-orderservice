package com.project.order_service.controller;

import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderUpdateRequest;
import com.project.order_service.dto.response.OrderDetailedResponse;
import com.project.order_service.entity.enums.OrderStatus;
import com.project.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDetailedResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderDetailedResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailedResponse> getOrderById(@PathVariable("id") UUID id) {
        OrderDetailedResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<OrderDetailedResponse>> getFilteredOrders(
            @RequestParam(required = false) List<OrderStatus> statuses,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @PageableDefault(size = 20, sort = "created_at") Pageable pageable
    ) {
        Page<OrderDetailedResponse> page = orderService.getFilteredOrders(statuses, startDate, endDate, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDetailedResponse>> getOrderByUserId(@PathVariable("userId") UUID userId) {
        List<OrderDetailedResponse> responses = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDetailedResponse> updateOrderById(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequest request) {
        OrderDetailedResponse response = orderService.updateOrderById(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Void> softDeleteOrderById(@PathVariable("id") UUID id) {
        orderService.softDeleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable("id") UUID id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
