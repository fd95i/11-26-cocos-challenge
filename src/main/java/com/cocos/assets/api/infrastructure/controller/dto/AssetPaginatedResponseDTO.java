package com.cocos.assets.api.infrastructure.controller.dto;

import java.util.List;

public record AssetPaginatedResponseDTO(
        Integer limit,
        Integer offset,
        Integer count,
        Integer totalCount,
        List<AssetResponseDTO> data
) {}
