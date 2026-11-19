package com.cocos.assets.api.infrastructure.data.db;

import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDataDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioDAO {
    BigDecimal getAvailableBalance(Integer userId);
    Integer getTotalSizeFor(Integer userId, Integer instrumentId);
    List<InstrumentSummaryDataDTO> getInstrumentSummary(Integer userId);
}
