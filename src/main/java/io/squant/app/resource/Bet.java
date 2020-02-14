package io.squant.app.resource;

import java.math.BigDecimal;

public class Bet {

    private final String winner;
    private final String loser;
    private final BigDecimal amount;
    private final String currency;
    private final String description;
    private final boolean paid;


    public Bet(String winner, String loser, BigDecimal amount, String currency, String description, boolean paid) {
        this.winner = winner;
        this.loser = loser;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.paid = paid;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPaid() {
        return paid;
    }
}
