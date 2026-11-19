package com.cocos.assets.api.application.usecase;

import com.cocos.assets.api.application.port.InstrumentsPort;
import com.cocos.assets.api.domain.entity.Asset;
import com.cocos.assets.api.domain.entity.AssetSearchCriteria;
import com.cocos.assets.api.domain.entity.AssetSearchResult;
import com.cocos.assets.api.infrastructure.data.db.dto.InstrumentDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAssetsUseCaseImpl {
    private static final Logger log = LoggerFactory.getLogger(GetAssetsUseCaseImpl.class);
    private final InstrumentsPort instrumentsPort;

    public GetAssetsUseCaseImpl(InstrumentsPort instrumentsPort) {
        this.instrumentsPort = instrumentsPort;
    }

    public AssetSearchResult getAssetsBy(AssetSearchCriteria criteria) {
        List<InstrumentDataDTO> instruments = instrumentsPort.getInstrumentsBy(criteria);
        Integer searchCount = instruments.stream().findFirst().map(InstrumentDataDTO::count).orElse(0);
        log.info("found {} assets matching criteria: name={}, ticker={} limit={} offset={}", searchCount, criteria.name(), criteria.ticker(), criteria.limit(), criteria.offset());
        return new AssetSearchResult(searchCount, instruments
                .stream()
                .map(data ->  new Asset(
                        data.id(), data.ticker(), data.name(), data.type()
                ))
                .toList());
    }
}
