package com.cocos.assets.api.infrastructure.controller.dto;

public record AssetPaginatedRequestDTO(
        Integer limit,
        Integer offset,
        String name,
        String ticker
) {
    public static AssetPaginatedRequestDTO by(
            Integer limit,
            Integer offset,
            String name,
            String ticker
    ) {
        return new AssetPaginatedRequestDTO(limit, offset, name, ticker);
    }
}
