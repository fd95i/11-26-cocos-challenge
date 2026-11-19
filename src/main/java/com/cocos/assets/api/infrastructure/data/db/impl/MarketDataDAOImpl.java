package com.cocos.assets.api.infrastructure.data.db.impl;

import com.cocos.assets.api.infrastructure.data.db.MarketDataDAO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.cocos.assets.api.infrastructure.data.db.sql.NeonDBConstants.GET_CLOSE_QUERY;

@Repository
public class MarketDataDAOImpl implements MarketDataDAO {
    private final JdbcClient neonClient;

    public MarketDataDAOImpl(JdbcClient neonClient) {
        this.neonClient = neonClient;
    }

    @Override
    public BigDecimal getCloseValueFor(Integer instrumentId) {
        return neonClient.sql(GET_CLOSE_QUERY)
                .param(instrumentId)
                .query(BigDecimal.class)
                .single();
    }
}
