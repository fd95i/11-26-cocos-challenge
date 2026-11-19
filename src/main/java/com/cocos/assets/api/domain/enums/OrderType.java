package com.cocos.assets.api.domain.enums;

import com.cocos.assets.api.infrastructure.config.exception.InvalidOrderException;

import java.util.Arrays;

public enum OrderType {
    MARKET,
    LIMIT;

    public static OrderType fromString(String type) {
        return Arrays.stream(OrderType.values())
                .filter(el -> el.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new InvalidOrderException("invalid order type"));
    }
}
