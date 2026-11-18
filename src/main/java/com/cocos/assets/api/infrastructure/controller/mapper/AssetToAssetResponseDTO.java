package com.cocos.assets.api.infrastructure.controller.mapper;

import com.cocos.assets.api.domain.entity.Asset;
import com.cocos.assets.api.infrastructure.controller.dto.AssetResponseDTO;

import java.util.function.Function;

public class AssetToAssetResponseDTO implements Function<Asset, AssetResponseDTO>
{
    @Override
    public AssetResponseDTO apply(Asset asset) {
        return new AssetResponseDTO(
                asset.id(),
                asset.ticker(),
                asset.name(),
                asset.type()
        );
    }
}
