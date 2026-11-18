package com.cocos.assets.api.infrastructure.data.db.dto;

public record InstrumentDataDTO(
        Integer id,
        String ticker,
        String name,
        String type,
        Integer count
) {}
