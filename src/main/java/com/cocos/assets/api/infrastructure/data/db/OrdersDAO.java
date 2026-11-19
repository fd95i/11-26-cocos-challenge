package com.cocos.assets.api.infrastructure.data.db;

import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;

public interface OrdersDAO {
    OrderDataDTO create(OrderDataDTO order);
}
