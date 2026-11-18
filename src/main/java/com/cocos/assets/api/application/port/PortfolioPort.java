package com.cocos.assets.api.application.port;

import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioPort {
    BigDecimal getAvailableBalanceFor(Integer userId);
    List<InstrumentSummaryDTO> getInstrumentSummaryFor(Integer userId);
}
