package com.cocos.assets.api.domain.entity;

public record Asset(
        Integer id,
        String ticker,
        String name,
        String type
) {}
