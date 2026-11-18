package com.cocos.assets.api.infrastructure.controller.mapper;

import com.cocos.assets.api.domain.entity.AssetSearchCriteria;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedRequestDTO;

import java.util.function.Function;

public class AssetPaginatedRequestToAssertSearchCriteria implements Function<AssetPaginatedRequestDTO, AssetSearchCriteria> {
    @Override
    public AssetSearchCriteria apply(AssetPaginatedRequestDTO dto) {
        return new AssetSearchCriteria(
                dto.limit(),
                dto.offset(),
                dto.name(),
                dto.ticker()
        );
    }
}
