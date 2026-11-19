package com.cocos.assets.api.application.port;

import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;

public interface OrdersPort {
    OrderDataDTO create(Order order);
}
