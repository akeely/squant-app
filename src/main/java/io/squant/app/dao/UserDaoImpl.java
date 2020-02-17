package io.squant.app.dao;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.squant.app.resource.User;

@Repository
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int put(User user) {

        String sql = "INSERT IGNORE INTO users (name, email) VALUES (:name, :email);";
        Map<String, String> params = Map.of("name", user.getName(), "email", user.getEmail());
        jdbcTemplate.update(sql, params);

        String getUserSql = "SELECT id FROM users WHERE email = :email";
        return jdbcTemplate.queryForObject(getUserSql, params, Integer.class);
    }
}
