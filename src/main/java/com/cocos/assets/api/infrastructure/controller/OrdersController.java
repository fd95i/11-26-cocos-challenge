package com.cocos.assets.api.infrastructure.controller;

import com.cocos.assets.api.infrastructure.adapter.in.CreateOrderHandler;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final CreateOrderHandler handler;

    public OrdersController(CreateOrderHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<PostOrderResponseDTO> createOrder(
            @RequestBody PostOrderRequestDTO dto
    ) {
        return ResponseEntity.ok(handler.apply(dto));
    }
}
