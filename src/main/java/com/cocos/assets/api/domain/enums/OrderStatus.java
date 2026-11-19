package com.cocos.assets.api.domain.enums;

import com.cocos.assets.api.domain.exception.InvalidOrderException;

import java.util.Arrays;

public enum OrderStatus {
    NEW,
    FILLED,
    REJECTED,
    CANCELED;

    public static OrderStatus fromString(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(el -> el.name().equals(status))
                .findFirst()
                .orElseThrow(() -> new InvalidOrderException("invalid order status"));
    }
}
