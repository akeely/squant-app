package io.squant.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.squant.app.dao.data.User;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final RowMapper<User> USER_ROW_MAPPER = new User.UserRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final LoadingCache<io.squant.app.resource.User, User> userCache;

    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCache = CacheBuilder.newBuilder()
                .maximumSize(1000L)
                .expireAfterAccess(2L, TimeUnit.DAYS)
                .concurrencyLevel(1)
                .build(new UserCacheLoader());
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", USER_ROW_MAPPER);
    }

    @Override
    public User findByExternalId(String externalId) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE externalId = :externalId",
                Map.of("externalId", externalId), USER_ROW_MAPPER);
    }

    @Override
    public User put(io.squant.app.resource.User user) {

        return userCache.getUnchecked(user);
    }

    private User getOrSave(io.squant.app.resource.User user) {

        Map<String, String> params = Map.of("name", user.getName(), "email", user.getEmail());
        try {
            LOG.info("Loading user {} from DB", user);
            String getUserSql = "SELECT * FROM users WHERE email = :email";
            return jdbcTemplate.queryForObject(getUserSql, params, USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("User not found. Creating new user {}", user);
            String externalId = UUID.randomUUID().toString();
            String sql = "INSERT INTO users (externalId, name, email) VALUES (:externalId, :name, :email);";
            Map<String, String> insertParams = new HashMap<>();
            insertParams.put("externalId", externalId);
            insertParams.putAll(params);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(sql, new MapSqlParameterSource(insertParams), keyHolder);
            int id = keyHolder.getKey().intValue();
            return new User(id, externalId, user.getName(), user.getEmail());
        }
    }

    private final class UserCacheLoader extends CacheLoader<io.squant.app.resource.User, User> {

        @Override
        public User load(io.squant.app.resource.User user) {
            return getOrSave(user);
        }
    }
}
