package io.squant.app.dao;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.squant.app.resource.User;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final LoadingCache<User, Integer> userCache;

    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCache = CacheBuilder.newBuilder()
                .maximumSize(1000L)
                .expireAfterAccess(2L, TimeUnit.DAYS)
                .concurrencyLevel(1)
                .build(new UserCacheLoader());
    }

    @Override
    public int put(User user) {

        return userCache.getUnchecked(user);
    }

    private int getOrSave(User user) {

        Map<String, String> params = Map.of("name", user.getName(), "email", user.getEmail());
        try {
            LOG.info("Loading user {} from DB", user);
            String getUserSql = "SELECT id FROM users WHERE email = :email";
            return jdbcTemplate.queryForObject(getUserSql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("User not found. Creating new user {}", user);
            String sql = "INSERT INTO users (name, email) VALUES (:name, :email);";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
            return keyHolder.getKey().intValue();
        }
    }

    private final class UserCacheLoader extends CacheLoader<User, Integer> {

        @Override
        public Integer load(User user) {
            return getOrSave(user);
        }
    }
}
