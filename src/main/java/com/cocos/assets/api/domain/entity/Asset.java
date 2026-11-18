package com.cocos.assets.api.domain.entity;

import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public record Asset(
        Integer instrumentId,
        String type,
        String ticker,
        String name,
        Integer size,
        BigDecimal totalValue,
        BigDecimal totalReturnRate,
        BigDecimal dailyReturnRate
) {
    public static Asset fromSummary(InstrumentSummaryDTO summary) {
        return new Asset(
                summary.instrumentid(),
                summary.instrumenttype(),
                summary.ticker(),
                summary.name(),
                summary.totalSize(),
                summary.totalValue(),
                summary.close().subtract(summary.avgprice()).divide(summary.avgprice(), MathContext.DECIMAL64).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN),
                summary.dailyReturnRate().setScale(2, RoundingMode.HALF_EVEN)
        );
    }
}
