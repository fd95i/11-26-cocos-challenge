package com.cocos.assets.api.application.port;

import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDataDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioPort {
    BigDecimal getAvailableBalanceFor(Integer userId);
    Integer getTotalSizeFor(Integer userId, Integer instrumentId);
    List<InstrumentSummaryDataDTO> getInstrumentSummaryFor(Integer userId);
}
