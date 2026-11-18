package com.cocos.assets.api.infrastructure.controller.dto;

import java.math.BigDecimal;

public record AssetSummaryResponseDTO(
        Integer instrumentId,
        String type,
        String ticker,
        String name,
        Integer size,
        BigDecimal totalValue,
        BigDecimal totalReturnRate,
        BigDecimal dailyReturnRate
) { }
