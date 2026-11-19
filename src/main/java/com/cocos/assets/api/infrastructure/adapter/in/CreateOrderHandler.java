package com.cocos.assets.api.infrastructure.adapter.in;

import com.cocos.assets.api.application.usecase.CreateOrderUseCaseImpl;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderRequestDTO;
import com.cocos.assets.api.infrastructure.controller.dto.PostOrderResponseDTO;
import com.cocos.assets.api.infrastructure.controller.mapper.OrderToPostOrderResponseDTO;
import com.cocos.assets.api.infrastructure.controller.mapper.PostOrderRequestDTOToOrder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CreateOrderHandler implements Function<PostOrderRequestDTO, PostOrderResponseDTO> {
    private final CreateOrderUseCaseImpl createOrderUseCaseImpl;
    private final PostOrderRequestDTOToOrder inboundMapper;
    private final OrderToPostOrderResponseDTO outboundMapper;

    public CreateOrderHandler(CreateOrderUseCaseImpl createOrderUseCaseImpl) {
        this.createOrderUseCaseImpl = createOrderUseCaseImpl;
        this.inboundMapper = new PostOrderRequestDTOToOrder();
        this.outboundMapper = new OrderToPostOrderResponseDTO();
    }

    @Override
    public PostOrderResponseDTO apply(PostOrderRequestDTO dto) {
        return outboundMapper.apply(createOrderUseCaseImpl.createOrder(inboundMapper.apply(dto)));
    }
}
