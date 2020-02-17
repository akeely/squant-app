package io.squant.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.squant.app.resource.Bet;

public class BetRowMapper implements RowMapper<Bet> {
    @Override
    public Bet mapRow(ResultSet resultSet, int i) throws SQLException {

        return new Bet(
                resultSet.getString("creatorName"),
                resultSet.getString("againstName"),
                resultSet.getString("winnerName"),
                resultSet.getBigDecimal("amount"),
                resultSet.getString("currency"),
                resultSet.getString("description"),
                resultSet.getBoolean("paid"));
    }
}
