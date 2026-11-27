package com.project.order_service.service.mapper;

import com.project.order_service.dto.response.ItemResponse;
import com.project.order_service.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    ItemResponse toDto(Item item);
}