package com.project.order_service.service;

import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderUpdateRequest;
import com.project.order_service.dto.response.OrderDetailedResponse;
import com.project.order_service.entity.Order;
import com.project.order_service.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDetailedResponse getOrderById(UUID id);
    OrderDetailedResponse createOrder(OrderCreateRequest request);
    Page<OrderDetailedResponse> getFilteredOrders(List<OrderStatus> statuses, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<OrderDetailedResponse> getOrdersByUserId(UUID userId);
    OrderDetailedResponse updateOrderById(UUID id, OrderUpdateRequest request);
    void softDeleteById(UUID id);
    void deleteOrderById(UUID id);
}
