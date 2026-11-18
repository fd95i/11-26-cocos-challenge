package com.cocos.assets.api.infrastructure.controller.dto;
import java.math.BigDecimal;
import java.util.List;

public record GetPortfolioResponseDTO(
        Integer userId,
        BigDecimal totalValue,
        BigDecimal available,
        List<AssetSummaryResponseDTO> assets
) {
}
