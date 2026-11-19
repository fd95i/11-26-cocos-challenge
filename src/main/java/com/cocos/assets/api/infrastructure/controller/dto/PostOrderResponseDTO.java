package com.cocos.assets.api.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PostOrderResponseDTO(
        Integer id,
        Integer instrumentId,
        Integer userId,
        Integer size,
        BigDecimal price,
        String side,
        String status,
        String type,
        Instant dateTime
) { }
