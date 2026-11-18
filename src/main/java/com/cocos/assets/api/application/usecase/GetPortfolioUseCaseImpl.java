package com.cocos.assets.api.application.usecase;

import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.domain.entity.Asset;
import com.cocos.assets.api.domain.entity.Portfolio;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class GetPortfolioUseCaseImpl {
    private final PortfolioPort portfolioPort;

    public GetPortfolioUseCaseImpl(PortfolioPort portfolioPort) {
        this.portfolioPort = portfolioPort;
    }

    public Portfolio getPortfolioFor(Integer userId) {
        List<InstrumentSummaryDTO> instruments = portfolioPort.getInstrumentSummaryFor(userId);
        BigDecimal balance = portfolioPort.getAvailableBalanceFor(userId);
        BigDecimal totalValue = balance.add(instruments.stream().map(InstrumentSummaryDTO::totalValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        List<Asset> assets = instruments.stream().filter(order -> order.totalSize() > 0).map(Asset::fromSummary).toList();

        return new Portfolio(
                userId,
                totalValue,
                balance,
                assets
        );
    }
}
