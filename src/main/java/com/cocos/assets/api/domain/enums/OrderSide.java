package com.cocos.assets.api.domain.enums;

import com.cocos.assets.api.infrastructure.config.exception.InvalidOrderException;

import java.util.Arrays;

public enum OrderSide {
    BUY,
    SELL,
    CASH_IN,
    CASH_OUT;

    public static OrderSide fromString(String side) {
        return Arrays.stream(OrderSide.values())
                .filter(el -> el.name().equals(side))
                .findFirst()
                .orElseThrow(() -> new InvalidOrderException("invalid order side"));
    }
}
