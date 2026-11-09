package com.project.order_service.service.mapper;

import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderUpdateRequest;
import com.project.order_service.dto.response.OrderDetailedResponse;
import com.project.order_service.dto.response.OrderResponse;
import com.project.order_service.dto.response.UserResponse;
import com.project.order_service.entity.Order;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {OrderItemMapper.class}
)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderCreateRequest request);

    OrderResponse toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateOrderFromDto(OrderUpdateRequest request, @MappingTarget Order order);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "user", source = "userDto")
    OrderDetailedResponse toDetailedDto(Order order, UserResponse userDto);
}
