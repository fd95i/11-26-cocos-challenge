package com.cocos.assets.api.infrastructure.data.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersRepositoryImpl {
    private JdbcTemplate neonClient;

    public UsersRepositoryImpl(JdbcTemplate neonClient) {
        this.neonClient = neonClient;
    }

    public List<String> getUsers() {
        return neonClient.queryForList("select id from users", String.class);
    }
}
