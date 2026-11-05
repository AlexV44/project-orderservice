package com.project.order_service;

import com.project.order_service.client.UserClient;
import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderUpdateRequest;
import com.project.order_service.dto.response.OrderDetailedResponse;
import com.project.order_service.dto.response.UserResponse;
import com.project.order_service.entity.Order;
import com.project.order_service.entity.OrderItem;
import com.project.order_service.entity.enums.OrderStatus;
import com.project.order_service.exception.notfound.OrderNotFoundException;
import com.project.order_service.repository.OrderRepository;
import com.project.order_service.service.OrderService;
import com.project.order_service.service.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        UUID userId = UUID.randomUUID();
        OrderCreateRequest request = mock(OrderCreateRequest.class);

        Order order = new Order();
        order.setUserId(userId);
        OrderItem item = new OrderItem();
        order.setItems(List.of(item));

        UserResponse userResponse = new UserResponse(userId, "test@mail.com", "John", "Doe");
        OrderDetailedResponse expectedResponse = mock(OrderDetailedResponse.class);

        when(orderMapper.toEntity(request)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(orderMapper.toDetailedDto(order, userResponse)).thenReturn(expectedResponse);

        OrderDetailedResponse actualResponse = orderService.createOrder(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(item.getOrder()).isEqualTo(order);

        verify(orderRepository).save(order);
        verify(userClient).getUserById(userId);
    }

    @Test
    void getOrderById_Success() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);

        UserResponse userResponse = new UserResponse(userId, "test@mail.com", "John", "Doe");
        OrderDetailedResponse expectedResponse = mock(OrderDetailedResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(orderMapper.toDetailedDto(order, userResponse)).thenReturn(expectedResponse);

        OrderDetailedResponse actualResponse = orderService.getOrderById(orderId);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(orderRepository).findById(orderId);
        verify(userClient).getUserById(userId);
        verify(orderMapper).toDetailedDto(order, userResponse);
    }

    @Test
    void getOrderById_NotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(OrderNotFoundException.class);

        verify(orderRepository).findById(orderId);
        verifyNoInteractions(userClient, orderMapper);
    }

    @Test
    void getOrdersByUserId_Success() {
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setUserId(userId);

        UserResponse userResponse = new UserResponse(userId, "test@mail.com", "John", "Doe");
        OrderDetailedResponse expectedResponse = mock(OrderDetailedResponse.class);

        when(orderRepository.getByUserId(userId)).thenReturn(List.of(order));
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(orderMapper.toDetailedDto(order, userResponse)).thenReturn(expectedResponse);

        List<OrderDetailedResponse> actualList = orderService.getOrdersByUserId(userId);

        assertThat(actualList).hasSize(1).contains(expectedResponse);
        verify(orderRepository).getByUserId(userId);
        verify(userClient).getUserById(userId);
    }

    @Test
    void updateOrderById_Success() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OrderUpdateRequest request = mock(OrderUpdateRequest.class);

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);

        UserResponse userResponse = new UserResponse(userId, "test@mail.com", "John", "Doe");
        OrderDetailedResponse expectedResponse = mock(OrderDetailedResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderMapper).updateOrderFromDto(request, order);
        when(userClient.getUserById(userId)).thenReturn(userResponse);
        when(orderMapper.toDetailedDto(order, userResponse)).thenReturn(expectedResponse);

        OrderDetailedResponse actualResponse = orderService.updateOrderById(orderId, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(orderMapper).updateOrderFromDto(request, order);
        verify(userClient).getUserById(userId);
    }

    @Test
    void softDeleteOrderById_Success() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setDeleted(false);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.softDeleteOrderById(orderId);

        assertThat(order.getDeleted()).isTrue();
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).deleteById(any());
    }

    @Test
    void deleteOrderById_Success() {
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrderById(orderId);

        verify(orderRepository).deleteById(orderId);
    }
}
