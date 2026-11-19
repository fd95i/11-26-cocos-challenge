package com.cocos.assets.api.application.port;

import java.math.BigDecimal;

public interface MarketDataPort {
    BigDecimal getCloseValueFor(Integer instrumentId);
}
