package com.cocos.assets.api.infrastructure.data.db.impl;

import com.cocos.assets.api.infrastructure.data.db.OrdersDAO;
import com.cocos.assets.api.infrastructure.data.db.dto.OrderDataDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static com.cocos.assets.api.infrastructure.data.db.sql.NeonDBConstants.INSERT_ORDER_QUERY;

@Repository
public class OrdersDAOImpl implements OrdersDAO {
    private final JdbcClient neonClient;

    public OrdersDAOImpl(JdbcClient neonClient) {
        this.neonClient = neonClient;
    }

    @Override
    public OrderDataDTO create(OrderDataDTO orderData) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        neonClient.sql(INSERT_ORDER_QUERY)
                .param("instrumentid", orderData.instrumentid())
                .param("userid", orderData.userid())
                .param("size", orderData.size())
                .param("price", orderData.price())
                .param("side", orderData.side())
                .param("status", orderData.status())
                .param("type", orderData.type())
                .param("datetime", orderData.datetime())
                .update(keyHolder);

        Map<String, Object> createdOrder = keyHolder.getKeys();
        return new ObjectMapper().convertValue(createdOrder, OrderDataDTO.class);
    }

}
