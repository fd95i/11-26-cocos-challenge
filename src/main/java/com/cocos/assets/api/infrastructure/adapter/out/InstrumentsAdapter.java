package com.cocos.assets.api.infrastructure.adapter.out;

import com.cocos.assets.api.application.port.InstrumentsPort;
import com.cocos.assets.api.domain.entity.AssetSearchCriteria;
import com.cocos.assets.api.infrastructure.data.db.InstrumentsDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentsAdapter implements InstrumentsPort {
    private final InstrumentsDAO dao;

    public InstrumentsAdapter(InstrumentsDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<InstrumentDataDTO> getInstrumentsBy(AssetSearchCriteria criteria) {
        return dao.getInstrumentsBy(
                criteria.name(),
                criteria.ticker(),
                criteria.limit(),
                criteria.offset()
        );
    }
}
