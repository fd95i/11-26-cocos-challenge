package com.cocos.assets.api.infrastructure.controller.mapper;

import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.domain.enums.OrderSide;
import com.cocos.assets.api.domain.enums.OrderType;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderRequestDTO;

import java.time.Instant;
import java.util.function.Function;

public class PostOrderRequestDTOToOrder implements Function<PostOrderRequestDTO, Order> {
    @Override
    public Order apply(PostOrderRequestDTO dto) {
        return Order.builder()
                .instrumentId(dto.instrumentId())
                .userId(dto.userId())
                .size(dto.size())
                .price(dto.price())
                .side(OrderSide.fromString(dto.side()))
                .type(OrderType.fromString(dto.type()))
                .dateTime(Instant.now())
                .build();
    }
}
