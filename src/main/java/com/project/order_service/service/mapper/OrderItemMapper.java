package com.project.order_service.service.mapper;

import com.project.order_service.dto.request.OrderItemRequest;
import com.project.order_service.dto.response.OrderItemResponse;
import com.project.order_service.entity.OrderItem;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
            uses = ItemMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toEntity(OrderItemRequest requestDto);

    @Mapping(target = "itemId", source = "item.id")
    OrderItemResponse toDto(OrderItem orderItem);
}
