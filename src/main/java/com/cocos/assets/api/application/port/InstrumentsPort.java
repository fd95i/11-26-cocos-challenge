package com.cocos.assets.api.application.port;

import com.cocos.assets.api.domain.entity.AssetSearchCriteria;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentDataDTO;

import java.util.List;

public interface InstrumentsPort {
    List<InstrumentDataDTO> getInstrumentsBy(AssetSearchCriteria criteria);
}
