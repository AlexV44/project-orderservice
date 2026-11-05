package com.project.order_service.service.impl;

import com.project.order_service.client.UserClient;
import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderUpdateRequest;
import com.project.order_service.dto.response.OrderDetailedResponse;
import com.project.order_service.entity.Order;
import com.project.order_service.entity.enums.OrderStatus;
import com.project.order_service.exception.notfound.OrderNotFoundException;
import com.project.order_service.repository.OrderRepository;
import com.project.order_service.repository.specification.OrderSpecification;
import com.project.order_service.service.OrderService;
import com.project.order_service.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserClient userClient;

    @Override
    public OrderDetailedResponse getOrderById(UUID id) {
        var order = findOrderOrThrow(id);
        var userResponse = userClient.getUserById(order.getUserId());
        return orderMapper.toDetailedDto(order, userResponse);
    }

    @Override
    @Transactional
    public OrderDetailedResponse createOrder(OrderCreateRequest request) {
        var order = orderMapper.toEntity(request);

        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }

        order.setStatus(OrderStatus.PENDING);

        var savedOrder = orderRepository.save(order);

        var userResponse = userClient.getUserById(order.getUserId());

        return orderMapper.toDetailedDto(savedOrder, userResponse);
    }

    @Override
    public Page<OrderDetailedResponse> getFilteredOrders(List<OrderStatus> statuses, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        var spec = Specification.where(OrderSpecification.hasStatus(statuses).and(OrderSpecification.createdBetween(startDate, endDate)));
        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);

        return ordersPage.map(order -> {
            var userResponse = userClient.getUserById(order.getUserId());
            return orderMapper.toDetailedDto(order, userResponse);
        });
    }

    @Override
    public List<OrderDetailedResponse> getOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepository.getByUserId(userId);
        return orders.stream().map(order -> {
            var userResponse = userClient.getUserById(order.getUserId());
            return orderMapper.toDetailedDto(order, userResponse);
        }).toList();
    }

    @Override
    @Transactional
    public OrderDetailedResponse updateOrderById(UUID id, OrderUpdateRequest request) {
        var order = findOrderOrThrow(id);
        orderMapper.updateOrderFromDto(request, order);
        var userResponse = userClient.getUserById(order.getUserId());
        return orderMapper.toDetailedDto(order, userResponse);
    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) {
        var order = findOrderOrThrow(id);
        order.setDeleted(true);
    }

    @Override
    @Transactional
    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }

    private Order findOrderOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
