package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.GetPortfolioHandler;
import com.cocos.assets.api.infrastructure.controller.dto.GetPortfolioResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);
    private final GetPortfolioHandler handler;

    public PortfolioController(
            GetPortfolioHandler handler
    ) {
        this.handler = handler;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetPortfolioResponseDTO> getPortfolio(
            @PathVariable Integer userId
    ) {
        log.info("GET /portfolio/{} Retrieving portfolio for userid={}", userId, userId);
        return ResponseEntity.ok(handler.apply(userId));
    }
}
