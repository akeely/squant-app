package io.squant.app.dao.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

public class Bet {

    private final int id;
    private final String externalId;
    private final long createTime;
    private final int creator;
    private final int against;
    private final Integer winner;
    private final BigDecimal amount;
    private final String currency;
    private final boolean paid;
    private final String description;
    private final boolean accepted;

    public Bet(int id, String externalId, long createTime, int creator, int against, Integer winner, BigDecimal amount,
            String currency, boolean paid, String description, boolean accepted) {
        this.id = id;
        this.externalId = externalId;
        this.createTime = createTime;
        this.creator = creator;
        this.against = against;
        this.winner = winner;
        this.amount = amount;
        this.currency = currency;
        this.paid = paid;
        this.description = description;
        this.accepted = accepted;
    }

    public Bet(int creator, int against, BigDecimal amount, String currency, String description) {

        // Generated
        this.externalId = UUID.randomUUID().toString();
        this.createTime = System.currentTimeMillis();

        // Set
        this.creator = creator;
        this.against = against;
        this.amount = amount;
        this.currency = currency;
        this.description = description;

        // Defaults
        this.id = 0;
        this.paid = false;
        this.accepted = false;
        this.winner = null;
    }

    public int getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getCreator() {
        return creator;
    }

    public int getAgainst() {
        return against;
    }

    public Integer getWinner() {
        return winner;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public static class BetRowMapper implements RowMapper<Bet> {

        @Override
        public Bet mapRow(ResultSet resultSet, int i) throws SQLException {

            return new Bet(resultSet.getInt("id"),
                    resultSet.getString("externalId"),
                    resultSet.getLong("createTime"),
                    resultSet.getInt("creator"),
                    resultSet.getInt("against"),
                    resultSet.getObject("winner", Integer.class),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getString("currency"),
                    resultSet.getBoolean("paid"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("accepted"));
        }
    }
}
