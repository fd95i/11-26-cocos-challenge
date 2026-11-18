package com.cocos.assets.api.infrastructure.adapter.in;

import com.cocos.assets.api.application.usecase.GetPortfolioUseCaseImpl;
import com.cocos.assets.api.infrastructure.controller.dto.GetPortfolioResponseDTO;
import com.cocos.assets.api.infrastructure.controller.mapper.PortfolioToResponseDTOMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GetPortfolioHandler implements Function<Integer, GetPortfolioResponseDTO> {
    private final GetPortfolioUseCaseImpl useCase;
    private final PortfolioToResponseDTOMapper mapper;

    public GetPortfolioHandler(GetPortfolioUseCaseImpl useCase) {
        this.useCase = useCase;
        this.mapper = new PortfolioToResponseDTOMapper();
    }

    @Override
    public GetPortfolioResponseDTO apply(Integer userId) {
        return mapper.apply(useCase.getPortfolioFor(userId));
    }
}
