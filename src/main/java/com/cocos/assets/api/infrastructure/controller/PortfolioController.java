package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.GetPortfolioHandler;
import com.cocos.assets.api.infrastructure.controller.dto.GetPortfolioResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
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
        return ResponseEntity.ok(handler.apply(userId));
    }
}
