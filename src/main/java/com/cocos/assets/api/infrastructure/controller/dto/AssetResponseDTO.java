package com.cocos.assets.api.infrastructure.controller.dto;

public record AssetResponseDTO(
        Integer id,
        String ticker,
        String name,
        String type
) {
}
