package com.cocos.assets.api.infrastructure.data.db.impl;

import com.cocos.assets.api.infrastructure.data.db.InstrumentsDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentDataDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cocos.assets.api.infrastructure.data.db.sql.NeonDBConstants.INSTRUMENTS_SEARCH_QUERY;

@Repository
public class InstrumentsDAOImpl implements InstrumentsDAO {
    private final JdbcClient neonClient;

    public InstrumentsDAOImpl(JdbcClient neonClient) {
        this.neonClient = neonClient;
    }

    @Override
    public List<InstrumentDataDTO> getInstrumentsBy(String name, String ticker, Integer limit, Integer offset) {
        return neonClient.sql(INSTRUMENTS_SEARCH_QUERY)
                .param("limit", limit)
                .param("offset",offset)
                .param("name", name)
                .param("ticker", ticker)
                .query(InstrumentDataDTO.class)
                .list();
    }
}
