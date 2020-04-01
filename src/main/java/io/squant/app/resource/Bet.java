package io.squant.app.resource;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Optional;

public class Bet {

    private final String id;
    private final User creator;
    private final User against;
    private final User winner;
    private final BigDecimal amount;
    private final String currency;
    private final String description;
    private final boolean paid;

    public Bet(String id,
            User creator,
            User against,
            User winner,
            BigDecimal amount,
            String currency,
            String description,
            boolean paid) {

        this.id = requireNonNull(id, "id");
        this.creator = requireNonNull(creator, "creator");
        this.against = requireNonNull(against, "against");
        this.winner = winner;
        this.amount = requireNonNull(amount, "amount");
        this.currency = requireNonNull(currency, "currency");
        this.description = requireNonNull(description, "description");
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public User getAgainst() {
        return against;
    }

    public Optional<User> getWinner() {
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
