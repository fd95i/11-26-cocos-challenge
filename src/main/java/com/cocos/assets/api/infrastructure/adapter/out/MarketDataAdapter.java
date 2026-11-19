package com.cocos.assets.api.infrastructure.adapter.out;

import com.cocos.assets.api.application.port.MarketDataPort;
import com.cocos.assets.api.infrastructure.data.db.MarketDataDAO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MarketDataAdapter implements MarketDataPort {
    private final MarketDataDAO marketDataDAO;

    public MarketDataAdapter(MarketDataDAO marketDataDAO) {
        this.marketDataDAO = marketDataDAO;
    }

    @Override
    public BigDecimal getCloseValueFor(Integer instrumentId) {
        return marketDataDAO.getCloseValueFor(instrumentId);
    }
}
