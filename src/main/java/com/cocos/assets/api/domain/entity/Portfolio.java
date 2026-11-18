package com.cocos.assets.api.domain.entity;


import java.math.BigDecimal;
import java.util.List;

public record Portfolio(
        Integer userId,
        BigDecimal totalValue,
        BigDecimal available,
        List<AssetSummary> assetSummaries
) { }
