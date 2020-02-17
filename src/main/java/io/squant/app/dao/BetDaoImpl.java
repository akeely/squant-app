package io.squant.app.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.squant.app.resource.Bet;
import io.squant.app.resource.Page;

@Repository
public class BetDaoImpl implements BetDao {

    private static final Logger LOG = LoggerFactory.getLogger(BetDaoImpl.class);

    private static final RowMapper<Bet> BET_MAPPER = new BetRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BetDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<Bet> find(int userId, boolean includePaid, int index, int size) {

        LOG.info("Getting bets for {} (include paid: {}). Page idx: {}, size: {}.", userId, includePaid, index, size);

        String sql = """
                SELECT c.name AS creatorName,
                       a.name AS againstName,
                       w.name AS winnerName,
                       b.amount,
                       b.currency,
                       b.description,
                       b.paid
                FROM bets b
                JOIN users c
                ON b.creator = c.id
                JOIN users a
                ON b.against = a.id
                LEFT JOIN users w
                ON b.winner = w.id
                WHERE (b.creator = :userId OR b.against = :userId)
                LIMIT :size
                OFFSET :index;
                """;

        Map<String, Object> params = Map.of("userId", userId, "index", index, "size", size);

        List<Bet> bets = jdbcTemplate.query(sql, params, BET_MAPPER);

        String countSql = "SELECT COUNT(*) FROM bets WHERE (creator = :userId OR against = :userId)";
        int count = jdbcTemplate.queryForObject(countSql, params, Integer.class);
        return new Page<>(bets, index, count);
    }
}
