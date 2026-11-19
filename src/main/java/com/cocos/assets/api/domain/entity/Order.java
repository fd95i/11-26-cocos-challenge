package com.cocos.assets.api.domain.entity;

import com.cocos.assets.api.domain.enums.OrderSide;
import com.cocos.assets.api.domain.enums.OrderStatus;
import com.cocos.assets.api.domain.enums.OrderType;

import java.math.BigDecimal;
import java.time.Instant;

public record Order(
        Integer id,
        Integer instrumentId,
        Integer userId,
        Integer size,
        BigDecimal price,
        OrderSide side,
        OrderStatus status,
        OrderType type,
        Instant dateTime
) {

    public static Builder builder() {
        return new Builder();
    }

    /** Builder precargado con los valores de una orden existente. */
    public Builder toBuilder() {
        return new Builder()
                .id(id)
                .instrumentId(instrumentId)
                .userId(userId)
                .size(size)
                .price(price)
                .side(side)
                .status(status)
                .type(type)
                .dateTime(dateTime);
    }

    public static final class Builder {
        private Integer id;
        private Integer instrumentId;
        private Integer userId;
        private Integer size;
        private BigDecimal price;
        private OrderSide side;
        private OrderStatus status;
        private OrderType type;
        private Instant dateTime;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder instrumentId(Integer instrumentId) {
            this.instrumentId = instrumentId;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder side(OrderSide side) {
            this.side = side;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(OrderType type) {
            this.type = type;
            return this;
        }

        public Builder dateTime(Instant dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Order build() {
            return new Order(id, instrumentId, userId, size, price, side, status, type, dateTime);
        }
    }
}
