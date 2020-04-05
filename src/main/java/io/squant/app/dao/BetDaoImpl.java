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
    public List<Bet> findAll(int index, int size) {
        LOG.info("Getting all bets. Page idx: {}, size: {}.", index, size);

        String sql = """
                SELECT *
                FROM bets b
                ORDER BY id
                LIMIT :size
                OFFSET :index;
                """;

        Map<String, Object> params = Map.of("index", index, "size", size);

        return jdbcTemplate.query(sql, params, BET_MAPPER);
    }

    @Override
    public int countAll() {
        String countSql = "SELECT COUNT(*) FROM bets;";
        return jdbcTemplate.queryForObject(countSql, Map.of(), Integer.class);
    }

    @Override
    public List<Bet> findByUser(int userId, int index, int size) {

        LOG.info("Getting bets for {}. Page idx: {}, size: {}.", userId, index, size);

        String sql = """
                SELECT *
                FROM bets b
                WHERE (b.creator = :userId OR b.against = :userId)
                ORDER BY id
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
                INSERT INTO bets (createTime, creator, against, amount, currency, description, externalId)
                VALUES (:createTime, :creator, :against, :amount, :currency, :description, :externalId);
                """;

        Map<String, Object> params = Map.of(
                "createTime", bet.getCreateTime(),
                "creator", bet.getCreator(),
                "against", bet.getAgainst(),
                "amount", bet.getAmount(),
                "currency", bet.getCurrency(),
                "description", bet.getDescription(),
                "externalId", bet.getExternalId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Bet findByExternalId(String externalId) {

        String sql = "SELECT * FROM bets WHERE externalId = :externalId;";
        Map<String, String> params = Map.of("externalId", externalId);
        return jdbcTemplate.queryForObject(sql, params, BET_MAPPER);
    }

    @Override
    public void setWinner(int id, int winnerId) {

        String sql = """
                UPDATE bets
                SET winner = :winnerId
                WHERE id = :id;
                """;

        Map<String, Object> params = Map.of("id", id, "winnerId", winnerId);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void markPaid(int id) {

        String sql = """
                UPDATE bets
                SET paid = 1
                WHERE id = :id;
                """;

        Map<String, Object> params = Map.of("id", id);

        jdbcTemplate.update(sql, params);

    }
}
