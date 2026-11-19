package com.cocos.assets.api.infrastructure.controller.dto;

import java.math.BigDecimal;

public record PostOrderRequestDTO(
        Integer instrumentId,
        Integer userId,
        Integer size,
        BigDecimal price,
        String side,
        String type
) {
}
