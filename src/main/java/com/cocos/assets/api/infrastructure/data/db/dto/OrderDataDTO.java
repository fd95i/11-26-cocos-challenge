package com.cocos.assets.api.infrastructure.data.db.dto;

import com.cocos.assets.api.domain.entity.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record OrderDataDTO(
        Integer id,
        Integer instrumentid,
        Integer userid,
        Integer size,
        BigDecimal price,
        String side,
        String status,
        String type,
        Timestamp datetime
) {
    public static OrderDataDTO fromOrder(Order order) {
        return new OrderDataDTO(
                order.id(),
                order.instrumentId(),
                order.userId(),
                order.size(),
                order.price(),
                order.side().name(),
                order.status().name(),
                order.type().name(),
                Timestamp.from(order.dateTime())
        );
    }
}
