package com.cocos.assets.api.infrastructure.data.db.impl;

import com.cocos.assets.api.infrastructure.data.db.PortfolioDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentSummaryDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.cocos.assets.api.infrastructure.data.db.sql.NeonDBConstants.BALANCE_QUERY;
import static com.cocos.assets.api.infrastructure.data.db.sql.NeonDBConstants.INSTRUMENTS_QUERY;

@Repository
public class PortfolioDAOImpl implements PortfolioDAO {
    private final JdbcClient neonClient;

    public PortfolioDAOImpl(JdbcClient neonClient) {
        this.neonClient = neonClient;
    }

    @Override
    public BigDecimal getAvailableBalance(Integer userId) {
        return neonClient.sql(BALANCE_QUERY)
                .param(userId)
                .query(BigDecimal.class)
                .optional().orElse(BigDecimal.ZERO);
    }

    @Override
    public List<InstrumentSummaryDTO> getInstrumentSummary(Integer userId) {
        return neonClient.sql(INSTRUMENTS_QUERY)
                .param(userId)
                .query(InstrumentSummaryDTO.class)
                .list();
    }
}
