package com.cocos.assets.api.infrastructure.controller.mapper;

import com.cocos.assets.api.domain.entity.Portfolio;
import com.cocos.assets.api.infrastructure.controller.dto.AssetResponseDTO;
import com.cocos.assets.api.infrastructure.controller.dto.GetPortfolioResponseDTO;

import java.util.function.Function;

public class PortfolioToResponseDTOMapper implements Function<Portfolio, GetPortfolioResponseDTO> {
    @Override
    public GetPortfolioResponseDTO apply(Portfolio portfolio) {
        return new GetPortfolioResponseDTO(
          portfolio.userId(),
          portfolio.totalValue(),
          portfolio.available(),
          portfolio.assets().stream().map( asset ->
              new AssetResponseDTO(
                      asset.instrumentId(),
                      asset.type(),
                      asset.ticker(),
                      asset.type(),
                      asset.size(),
                      asset.totalValue(),
                      asset.totalReturnRate(),
                      asset.dailyReturnRate()
              )).toList()
        );
    }
}
