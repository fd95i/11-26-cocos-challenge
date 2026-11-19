package com.cocos.assets.api.application.usecase;

import com.cocos.assets.api.application.port.MarketDataPort;
import com.cocos.assets.api.application.port.OrdersPort;
import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.domain.enums.OrderSide;
import com.cocos.assets.api.domain.enums.OrderStatus;
import com.cocos.assets.api.domain.enums.OrderType;
import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.cocos.assets.api.domain.enums.OrderSide.*;
import static com.cocos.assets.api.domain.enums.OrderStatus.*;
import static com.cocos.assets.api.domain.enums.OrderType.MARKET;

@Component
public class CreateOrderUseCase {
    private static final Integer ARS_INSTRUMENT_ID = 66;
    private final OrdersPort ordersPort;
    private final PortfolioPort portfolioPort;
    private final MarketDataPort marketDataPort;

    public CreateOrderUseCase(
            OrdersPort ordersPort,
            PortfolioPort portfolioPort,
            MarketDataPort marketDataPort
    ) {
        this.ordersPort = ordersPort;
        this.portfolioPort = portfolioPort;
        this.marketDataPort = marketDataPort;
    }

    public Order createOrder(Order order) {
        Order orderWithPrice = setOrderPrice(order);
        Order orderWithStatus = setOrderStatus(orderWithPrice);

        boolean valid = isOrderValid(orderWithStatus);
        if (!valid) {
            orderWithStatus = orderWithStatus.toBuilder()
                    .status(REJECTED)
                    .build();
        }
        OrderDataDTO createdOrder = ordersPort.create(orderWithStatus);
        return Order.builder()
                .id(createdOrder.id())
                .instrumentId(createdOrder.instrumentid())
                .userId(createdOrder.userid())
                .size(createdOrder.size())
                .price(createdOrder.price())
                .side(OrderSide.fromString(createdOrder.side()))
                .status(OrderStatus.fromString(createdOrder.status()))
                .type(OrderType.fromString(createdOrder.type()))
                .dateTime(createdOrder.datetime().toInstant())
                .build();
    }
    
    public Order setOrderStatus(Order order) {
        return order.toBuilder()
                .status(MARKET.equals(order.type())? FILLED : NEW)
                .build();
    }

    public Order setOrderPrice(Order order) {
        if (MARKET.equals(order.type()) && isARSOrder.negate().test(order))
            return order.toBuilder().price(marketDataPort.getCloseValueFor(order.instrumentId())).build();
        if (isARSOrder.test(order))
            return order.toBuilder().price(BigDecimal.ONE).build();
        return order;
    }

    // falta validar tenencia de acciones
    public boolean isOrderValid(Order order) {
        if (order.side() == null || order.status() == null || order.type() == null)
            return false;

        BigDecimal availableBalance = portfolioPort.getAvailableBalanceFor(order.userId());

        Integer totalSize = 0;
        if (SELL.equals(order.side()))
            totalSize = portfolioPort.getTotalSizeFor(order.userId(), order.instrumentId());

        return isOrderSideValid.and(isOrderPricePresent).test(order) &&
                isBalanceEnough.test(order, availableBalance) &&
                isSizeEnough.test(order, totalSize);
    }

    private final Predicate<Order> isARSOrder = order -> ARS_INSTRUMENT_ID.equals(order.instrumentId());

    private final Predicate<Order> isOrderSideValid = order -> (isARSOrder.negate().test(order) && List.of(BUY, SELL).contains(order.side())) || (isARSOrder.test(order) && List.of(CASH_IN, CASH_OUT).contains(order.side()));
    private final Predicate<Order> isOrderPricePresent = order -> order.price() != null;
    private final BiPredicate<Order, BigDecimal> isBalanceEnough = (order,balance) -> List.of(CASH_IN, SELL).contains(order.side()) || (List.of(CASH_OUT, BUY).contains(order.side()) && balance.subtract(order.price().multiply(BigDecimal.valueOf(order.size()))).doubleValue() >= 0);
    private final BiPredicate<Order, Integer> isSizeEnough = (order,totalSize) -> List.of(CASH_IN, CASH_OUT, BUY).contains(order.side()) || (SELL.equals(order.side()) && totalSize >= order.size());
}
