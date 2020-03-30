package io.squant.app.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.squant.app.dao.data.Bet;

@Repository
public class BetDaoImpl implements BetDao {

    private static final Logger LOG = LoggerFactory.getLogger(BetDaoImpl.class);

    private static final RowMapper<Bet> BET_MAPPER = new Bet.BetRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BetDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bet> findByUser(int userId, int index, int size) {

        LOG.info("Getting bets for {}. Page idx: {}, size: {}.", userId, index, size);

        String sql = """
                SELECT *
                FROM bets b
                WHERE (b.creator = :userId OR b.against = :userId)
                LIMIT :size
                OFFSET :index;
                """;

        Map<String, Object> params = Map.of("userId", userId, "index", index, "size", size);

        return jdbcTemplate.query(sql, params, BET_MAPPER);
    }

    @Override
    public int countByUser(int userId) {
        Map<String, Object> params = Map.of("userId", userId);
        String countSql = "SELECT COUNT(*) FROM bets WHERE (creator = :userId OR against = :userId)";
        return jdbcTemplate.queryForObject(countSql, params, Integer.class);
    }

    @Override
    public void save(Bet bet) {

        LOG.info("Saving bet by {} against {} for {} {}.", bet.getCreator(), bet.getAgainst(), bet.getAmount(),
                bet.getCurrency());

        String sql = """
                INSERT INTO bets (createTime, creator, against, amount, currency, description)
                VALUES (:createTime, :creator, :against, :amount, :currency, :description);
                """;

        Map<String, Object> params = Map.of(
                "createTime", bet.getCreateTime(),
                "creator", bet.getCreator(),
                "against", bet.getAgainst(),
                "amount", bet.getAmount(),
                "currency", bet.getCurrency(),
                "description", bet.getDescription());

        jdbcTemplate.update(sql, params);
    }
}
