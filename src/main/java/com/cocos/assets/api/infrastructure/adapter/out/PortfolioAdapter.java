package com.cocos.assets.api.infrastructure.adapter.out;

import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.infrastructure.data.db.PortfolioDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PortfolioAdapter implements PortfolioPort {
    private final PortfolioDAO dao;

    public PortfolioAdapter(PortfolioDAO dao) {
        this.dao = dao;
    }

    @Override
    public BigDecimal getAvailableBalanceFor(Integer userId) {
        return dao.getAvailableBalance(userId);
    }

    @Override
    public List<InstrumentSummaryDTO> getInstrumentSummaryFor(Integer userId) {
        return dao.getInstrumentSummary(userId);
    }
}
