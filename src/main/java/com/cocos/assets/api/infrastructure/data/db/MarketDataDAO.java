package com.cocos.assets.api.infrastructure.data.db;

import java.math.BigDecimal;

public interface MarketDataDAO {
    BigDecimal getCloseValueFor(Integer instrumentId);
}
