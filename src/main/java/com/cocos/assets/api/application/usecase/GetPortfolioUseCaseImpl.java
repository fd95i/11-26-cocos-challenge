package com.cocos.assets.api.application.usecase;

import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.domain.entity.AssetSummary;
import com.cocos.assets.api.domain.entity.Portfolio;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class GetPortfolioUseCaseImpl {
    private static final Logger log = LoggerFactory.getLogger(GetPortfolioUseCaseImpl.class);
    private final PortfolioPort portfolioPort;

    public GetPortfolioUseCaseImpl(PortfolioPort portfolioPort) {
        this.portfolioPort = portfolioPort;
    }

    public Portfolio getPortfolioFor(Integer userId) {
        List<InstrumentSummaryDataDTO> instruments = portfolioPort.getInstrumentSummaryFor(userId);
        BigDecimal balance = portfolioPort.getAvailableBalanceFor(userId);
        BigDecimal totalValue = balance.add(instruments.stream().map(InstrumentSummaryDataDTO::totalValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        List<AssetSummary> assetSummaries = instruments.stream().filter(order -> order.totalSize() > 0).map(AssetSummary::fromSummary).toList();
        log.debug("found portfolio for userId={}, availableBalance={} and totalValue={}", userId, balance, totalValue);
        return new Portfolio(
                userId,
                totalValue,
                balance,
                assetSummaries
        );
    }
}
