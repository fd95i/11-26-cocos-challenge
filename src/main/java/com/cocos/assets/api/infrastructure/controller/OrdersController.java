package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.CreateOrderHandler;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);
    private final CreateOrderHandler handler;

    public OrdersController(CreateOrderHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<PostOrderResponseDTO> createOrder(
            @RequestBody PostOrderRequestDTO dto
    ) {
        log.info("POST /orders - new order recieved for userId {} on instrumentId {} and side {}", dto.userId(), dto.instrumentId(), dto.side());
        return ResponseEntity.ok(handler.apply(dto));
    }
}
