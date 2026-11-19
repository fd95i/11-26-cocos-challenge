package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.GetAssetsHandler;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.AssetPaginatedResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetsController {
    private static final Logger log = LoggerFactory.getLogger(AssetsController.class);
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
        log.info("GET /assets - retrieving assets with criteria: name={}, ticker={} limit={} offset={}", name,ticker,limit,offset);
        return ResponseEntity.ok(handler.apply(AssetPaginatedRequestDTO.by(limit, offset, name, ticker)));
    }
}
