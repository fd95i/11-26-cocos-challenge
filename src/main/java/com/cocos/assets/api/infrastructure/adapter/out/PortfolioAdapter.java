package com.cocos.assets.api.infrastructure.adapter.out;

import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.infrastructure.data.db.PortfolioDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDataDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
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
    public List<InstrumentSummaryDataDTO> getInstrumentSummaryFor(Integer userId) {
        return dao.getInstrumentSummary(userId);
    }
}
