package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.GetAssetsHandler;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetsController {
    private final GetAssetsHandler handler;

    public AssetsController(GetAssetsHandler handler) {
        this.handler = handler;
    }

    @GetMapping
    public ResponseEntity<AssetPaginatedResponseDTO> getAssetsBy(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset
    ) {
        return ResponseEntity.ok(handler.apply(AssetPaginatedRequestDTO.by(limit, offset, name, ticker)));
    }
}
