package io.squant.app.resource;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Optional;

public class Bet {

    private final String creator;
    private final String against;
    private final String winner;
    private final BigDecimal amount;
    private final String currency;
    private final String description;
    private final boolean paid;


    public Bet(String creator, String against, String winner, BigDecimal amount, String currency, String description, boolean paid) {
        this.creator = requireNonNull(creator, "creator");
        this.against = requireNonNull(against, "against");
        this.winner = winner;
        this.amount = requireNonNull(amount, "amount");
        this.currency = requireNonNull(currency, "currency");
        this.description = requireNonNull(description, "description");
        this.paid = paid;
    }

    public String getCreator() {
        return creator;
    }

    public String getAgainst() {
        return against;
    }

    public Optional<String> getWinner() {
        return Optional.ofNullable(winner);
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
