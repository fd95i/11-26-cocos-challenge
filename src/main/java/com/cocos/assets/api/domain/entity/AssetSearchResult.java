package com.cocos.assets.api.domain.entity;

import java.util.List;

public record AssetSearchResult(
        Integer totalCount,
        List<Asset> assets
) {
}
