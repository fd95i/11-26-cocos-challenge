package com.cocos.assets.api.infrastructure.data.db;

import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentDataDTO;

import java.util.List;

public interface InstrumentsDAO {
    List<InstrumentDataDTO> getInstrumentsBy(String name, String ticker, Integer limit, Integer offset);
}
