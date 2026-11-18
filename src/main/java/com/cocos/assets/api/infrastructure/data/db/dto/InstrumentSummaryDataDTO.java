package com.cocos.assets.api.infrastructure.data.db.dto;

import java.math.BigDecimal;

public record InstrumentSummaryDataDTO(
         Integer userid,
         Integer totalSize,
         Integer instrumentid,
         String instrumenttype,
         String ticker,
         String name,
         BigDecimal close,
         BigDecimal totalValue,
         BigDecimal avgprice,
         BigDecimal dailyReturnRate
) { }
