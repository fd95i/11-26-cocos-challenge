package com.cocos.assets.api.infrastructure.controller.mapper;

import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderResponseDTO;

import java.util.function.Function;

public class OrderToPostOrderResponseDTO implements Function<Order, PostOrderResponseDTO> {
    @Override
    public PostOrderResponseDTO apply(Order order) {
        return new PostOrderResponseDTO(
                order.id(),
                order.instrumentId(),
                order.userId(),
                order.size(),
                order.price(),
                order.side().name(),
                order.status().name(),
                order.type().name(),
                order.dateTime()
        );
    }
}
