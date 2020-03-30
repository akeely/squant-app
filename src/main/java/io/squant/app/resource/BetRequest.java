package io.squant.app.resource;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BetRequest {

    private final String against;
    private final BigDecimal amount;
    private final String currency;
    private final String description;

    @JsonCreator
    public BetRequest(@JsonProperty("against") String against,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("currency") String currency,
            @JsonProperty("description") String description) {
        this.against = requireNonNull(against, "against");
        this.amount = requireNonNull(amount, "amount");
        this.currency = requireNonNull(currency, "currency");
        this.description = requireNonNull(description, "description");
    }

    public String getAgainst() {
        return against;
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
}
