package com.cocos.assets.api.infrastructure.adapter.out;

import com.cocos.assets.api.application.port.OrdersPort;
import com.cocos.assets.api.domain.entity.Order;
import com.cocos.assets.api.infrastructure.data.db.OrdersDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;
import org.springframework.stereotype.Service;

@Service
public class OrdersAdapter implements OrdersPort {
    private final OrdersDAO ordersDAO;

    public OrdersAdapter(OrdersDAO ordersDAO) {
        this.ordersDAO = ordersDAO;
    }

    @Override
    public OrderDataDTO create(Order order) {
        return ordersDAO.create(OrderDataDTO.fromOrder(order));
    }
}
