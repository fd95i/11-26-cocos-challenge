package com.cocos.assets.api.application.usecase;

import com.cocos.assets.api.application.port.MarketDataPort;
import com.cocos.assets.api.application.port.OrdersPort;
import com.cocos.assets.api.application.port.PortfolioPort;
import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.domain.enums.OrderSide;
import com.cocos.assets.api.domain.enums.OrderType;
import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static com.cocos.assets.api.domain.enums.OrderSide.*;
import static com.cocos.assets.api.domain.enums.OrderStatus.*;
import static com.cocos.assets.api.domain.enums.OrderType.LIMIT;
import static com.cocos.assets.api.domain.enums.OrderType.MARKET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseImplTest {

    private static final Integer USER_ID = 1;
    private static final Integer STOCK_ID = 47;
    private static final Integer ARS_ID = 66;
    private static final BigDecimal CLOSE = new BigDecimal("100.00");
    private static final Instant NOW = Instant.parse("2023-07-12T12:00:00Z");

    @Mock private OrdersPort ordersPort;
    @Mock private PortfolioPort portfolioPort;
    @Mock private MarketDataPort marketDataPort;
    @Captor private ArgumentCaptor<Order> orderCaptor;

    private CreateOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateOrderUseCaseImpl(ordersPort, portfolioPort, marketDataPort);
        lenient().when(ordersPort.create(any(Order.class)))
                .thenAnswer(inv -> toDto(inv.getArgument(0)));
        lenient().when(marketDataPort.getCloseValueFor(STOCK_ID)).thenReturn(CLOSE);
        lenient().when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("1000000"));
        lenient().when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(0);
    }

    // ---------------------------------------------------------------- MARKET BUY

    @Test
    void whenMarketBuyingShouldBeFilledWithClosePrice() {
        Order result = useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, null));

        Order persisted = persisted();
        assertThat(persisted.status()).isEqualTo(FILLED);
        assertThat(persisted.price()).isEqualByComparingTo(CLOSE);
        assertThat(result.status()).isEqualTo(FILLED);
    }

    @Test
    void whenMarketBuyingPriceShouldBeIgnoredAndCloseShouldBeUsed() {
        useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, new BigDecimal("1.00")));

        assertThat(persisted().price()).isEqualByComparingTo(CLOSE);
    }

    @Test
    void whenMarketBuyingWithExactBalanceShouldBeFilled() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("1000.00"));

        useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, null)); // 10 x 100 = 1000

        assertThat(persisted().status()).isEqualTo(FILLED);
    }

    @Test
    void whenMarketBuyingWithoutBalanceShouldBeRejected() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("999.99"));

        Order result = useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
        assertThat(result.status()).isEqualTo(REJECTED);
    }

    @Test
    void whenMarketBuyingWithoutClosePriceShouldBeRejected() {
        when(marketDataPort.getCloseValueFor(STOCK_ID)).thenReturn(null);

        useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    // --------------------------------------------------------------- MARKET SELL

    @Test
    void whenMarketSellingWithEnoughSizeShouldBeFilled() {
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(50);

        useCase.createOrder(order(SELL, MARKET, STOCK_ID, 30, null));

        Order persisted = persisted();
        assertThat(persisted.status()).isEqualTo(FILLED);
        assertThat(persisted.price()).isEqualByComparingTo(CLOSE);
    }

    @Test
    void whenMarketSellingWithExactSizeShouldBeFilled() {
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(30);

        useCase.createOrder(order(SELL, MARKET, STOCK_ID, 30, null));

        assertThat(persisted().status()).isEqualTo(FILLED);
    }

    @Test
    void whenMarketSellingWithoutSizeShouldBeRejected() {
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(29);

        useCase.createOrder(order(SELL, MARKET, STOCK_ID, 30, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    @Test
    void whenMarketSellingInstrumentsBalanceIsntRequired() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(BigDecimal.ZERO);
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(10);

        useCase.createOrder(order(SELL, MARKET, STOCK_ID, 10, null));

        assertThat(persisted().status()).isEqualTo(FILLED);
    }

    // --------------------------------------------------------------------- LIMIT BUY

    @Test
    void whenLimitBuyingWithEnoughBalanceShouldBeNew() {
        BigDecimal limitPrice = new BigDecimal("90.50");

        useCase.createOrder(order(BUY, LIMIT, STOCK_ID, 10, limitPrice));

        Order persisted = persisted();
        assertThat(persisted.status()).isEqualTo(NEW);
        assertThat(persisted.price()).isEqualByComparingTo(limitPrice);
        verifyNoInteractions(marketDataPort);
    }

    @Test
    void whenLimitBuyingWithoutBalanceShouldBeRejected() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("100"));

        useCase.createOrder(order(BUY, LIMIT, STOCK_ID, 10, new BigDecimal("90.50")));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    @Test
    void whenLimitBuyingWithoutPriceShouldBeRejected() {
        useCase.createOrder(order(BUY, LIMIT, STOCK_ID, 10, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    // --------------------------------------------------------------------- LIMIT SELL

    @Test
    void whenLimitSellingWithEnoughSizeShouldBeNew() {
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(10);

        useCase.createOrder(order(SELL, LIMIT, STOCK_ID, 10, new BigDecimal("120")));

        assertThat(persisted().status()).isEqualTo(NEW);
    }

    @Test
    void whenLimitSellingWithoutSizeShouldBeRejected() {
        when(portfolioPort.getTotalSizeFor(USER_ID, STOCK_ID)).thenReturn(5);

        useCase.createOrder(order(SELL, LIMIT, STOCK_ID, 10, new BigDecimal("120")));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    // ------------------------------------------------------------- CASH_IN / OUT

    @Test
    void whenMarketCashInTransferPriceShouldBe1AndStatusFilled() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(BigDecimal.ZERO);

        useCase.createOrder(order(CASH_IN, MARKET, ARS_ID, 5000, null));

        Order persisted = persisted();
        assertThat(persisted.status()).isEqualTo(FILLED);
        assertThat(persisted.price()).isEqualByComparingTo(BigDecimal.ONE);
        verifyNoInteractions(marketDataPort);
    }

    @Test
    void whenLimitCashInTransferPriceShouldBe1AndStatusNew() {
        useCase.createOrder(order(CASH_IN, LIMIT, ARS_ID, 5000, new BigDecimal("999")));

        Order persisted = persisted();
        assertThat(persisted.status()).isEqualTo(NEW);
        assertThat(persisted.price()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void whenCashOutTransferWithEnoughBalanceShouldBeFilled() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("5000"));

        useCase.createOrder(order(CASH_OUT, MARKET, ARS_ID, 5000, null)); // saldo exacto

        assertThat(persisted().status()).isEqualTo(FILLED);
    }

    @Test
    void whenCashOutTransferWithoutBalanceShouldBeRejected() {
        when(portfolioPort.getAvailableBalanceFor(USER_ID)).thenReturn(new BigDecimal("4999"));

        useCase.createOrder(order(CASH_OUT, MARKET, ARS_ID, 5000, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    // ------------------------------------------------- UNHAPPY PATH

    @ParameterizedTest
    @CsvSource({
            "47, CASH_IN",
            "47, CASH_OUT",
            "66, BUY",
            "66, SELL"
    })
    void whenSideIsIncompatibleWithInstrumentIdShouldBeRejected(int instrumentId, OrderSide side) {
        useCase.createOrder(order(side, MARKET, instrumentId, 1, null));

        assertThat(persisted().status()).isEqualTo(REJECTED);
    }

    // ------------------------------------------------------------------- MAPEO

    @Test
    void whenOrderIsPersistedDataShouldBeEqualToPersistedModel() {
        OrderDataDTO stored = new OrderDataDTO(
                123, STOCK_ID, USER_ID, 10, new BigDecimal("100.00"),
                "BUY", "FILLED", "MARKET", Timestamp.from(NOW));
        doReturn(stored).when(ordersPort).create(any(Order.class));

        Order result = useCase.createOrder(order(BUY, MARKET, STOCK_ID, 10, null));

        assertThat(result.id()).isEqualTo(123);
        assertThat(result.instrumentId()).isEqualTo(STOCK_ID);
        assertThat(result.userId()).isEqualTo(USER_ID);
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.price()).isEqualByComparingTo("100.00");
        assertThat(result.side()).isEqualTo(BUY);
        assertThat(result.status()).isEqualTo(FILLED);
        assertThat(result.type()).isEqualTo(MARKET);
        assertThat(result.dateTime()).isEqualTo(NOW);
    }

    // ------------------------------------------- MÉTODOS PÚBLICOS DIRECTOS

    @Test
    void whenSetOrderStatusMarketIsFilledAndLimitIsNew() {
        assertThat(useCase.setOrderStatus(order(BUY, MARKET, STOCK_ID, 1, null)).status()).isEqualTo(FILLED);
        assertThat(useCase.setOrderStatus(order(BUY, LIMIT, STOCK_ID, 1, null)).status()).isEqualTo(NEW);
    }

    @Test
    void whenSetOrderPriceMarketShouldUseClose() {
        Order result = useCase.setOrderPrice(order(BUY, MARKET, STOCK_ID, 1, null));

        assertThat(result.price()).isEqualByComparingTo(CLOSE);
    }

    @Test
    void whenSetOrderPriceLimitShouldKeepOrderPrice() {
        BigDecimal price = new BigDecimal("77.7");

        Order result = useCase.setOrderPrice(order(BUY, LIMIT, STOCK_ID, 1, price));

        assertThat(result.price()).isEqualByComparingTo(price);
        verifyNoInteractions(marketDataPort);
    }

    @ParameterizedTest
    @CsvSource({"MARKET", "LIMIT"})
    void whenSetOrderPriceOnARSPriceShouldBe1(OrderType type) {
        Order result = useCase.setOrderPrice(order(CASH_IN, type, ARS_ID, 1, new BigDecimal("55")));

        assertThat(result.price()).isEqualByComparingTo(BigDecimal.ONE);
        verifyNoInteractions(marketDataPort);
    }

    @Test
    void whenOrderFieldsAreNullIsOrderValidShouldReturnFalse() {
        Order base = order(BUY, MARKET, STOCK_ID, 1, CLOSE).toBuilder().status(FILLED).build();

        assertThat(useCase.isOrderValid(base.toBuilder().side(null).build())).isFalse();
        assertThat(useCase.isOrderValid(base.toBuilder().status(null).build())).isFalse();
        assertThat(useCase.isOrderValid(base.toBuilder().type(null).build())).isFalse();
        verifyNoInteractions(portfolioPort);
    }

    // ----------------------------------------------------------------- helpers

    private Order order(OrderSide side, OrderType type, Integer instrumentId, int size, BigDecimal price) {
        return Order.builder()
                .instrumentId(instrumentId)
                .userId(USER_ID)
                .size(size)
                .price(price)
                .side(side)
                .type(type)
                .dateTime(NOW)
                .build();
    }

    private Order persisted() {
        verify(ordersPort).create(orderCaptor.capture());
        return orderCaptor.getValue();
    }

    private OrderDataDTO toDto(Order o) {
        return new OrderDataDTO(
                999, o.instrumentId(), o.userId(), o.size(), o.price(),
                o.side() == null ? null : o.side().name(),
                o.status().name(),
                o.type() == null ? null : o.type().name(),
                Timestamp.from(NOW));
    }
}
