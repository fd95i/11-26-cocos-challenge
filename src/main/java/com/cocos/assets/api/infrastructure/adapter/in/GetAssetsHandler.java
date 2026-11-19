package com.cocos.assets.api.infrastructure.adapter.in;


import com.cocos.assets.api.application.usecase.GetAssetsUseCaseImpl;
import com.cocos.assets.api.domain.entity.AssetSearchCriteria;
import com.cocos.assets.api.domain.entity.AssetSearchResult;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedResponseDTO;
import com.cocos.assets.api.infrastructure.controller.mapper.AssetPaginatedRequestToAssertSearchCriteria;
import com.cocos.assets.api.infrastructure.controller.mapper.AssetToAssetResponseDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GetAssetsHandler implements Function<AssetPaginatedRequestDTO, AssetPaginatedResponseDTO> {
    private final GetAssetsUseCaseImpl useCase;
    private final AssetPaginatedRequestToAssertSearchCriteria inboundMapper;
    private final AssetToAssetResponseDTO outboundMapper;

    public GetAssetsHandler(GetAssetsUseCaseImpl useCase) {
        this.useCase = useCase;
        this.inboundMapper = new AssetPaginatedRequestToAssertSearchCriteria();
        this.outboundMapper = new AssetToAssetResponseDTO();
    }

    @Override
    public AssetPaginatedResponseDTO apply(AssetPaginatedRequestDTO dto) {
        AssetSearchCriteria criteria = inboundMapper.apply(dto);
        AssetSearchResult searchResult = useCase.getAssetsBy(criteria);
        return new AssetPaginatedResponseDTO(
                criteria.limit(),
                criteria.offset(),
                searchResult.assets().size(),
                searchResult.totalCount(),
                searchResult.assets().stream().map(outboundMapper).toList()
        );
    }
}
