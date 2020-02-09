package io.squant.core.bet;

import java.util.UUID;

import io.squant.core.user.User;

public class Bet {

    public enum Status {
        PROPOSED, ACCEPTED, DECLINED, CANCELLED, COMPLETED, PAID;
    }

    private final String id;
    private final String name;
    private final String odds;
    private final String stakes;
    private final User proposedBy;
    private final User proposedTo;
    private final Status status;

    public Bet(String id, String name, String odds, String stakes, User proposedBy, User proposedTo, Status status) {
        this.id = id;
        this.name = name;
        this.odds = odds;
        this.stakes = stakes;
        this.proposedBy = proposedBy;
        this.proposedTo = proposedTo;
        this.status = status;
    }

    /**
     * Create a new bet.
     */
    public Bet(String name, String odds, String stakes, User proposedBy, User proposedTo) {

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.odds = odds;
        this.stakes = stakes;
        this.proposedBy = proposedBy;
        this.proposedTo = proposedTo;
        this.status = Status.PROPOSED;
    }

    /**
     * Copy c-tor used to update the status of a bet.
     */
    public Bet(Bet bet, Status status) {

        this.id = bet.getId();
        this.name = bet.getName();
        this.odds = bet.getOdds();
        this.stakes = bet.getStakes();
        this.proposedBy = bet.getProposedBy();
        this.proposedTo = bet.getProposedTo();
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOdds() {
        return odds;
    }

    public String getStakes() {
        return stakes;
    }

    public User getProposedBy() {
        return proposedBy;
    }

    public User getProposedTo() {
        return proposedTo;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bet{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", odds='").append(odds).append('\'');
        sb.append(", stakes='").append(stakes).append('\'');
        sb.append(", proposedBy=").append(proposedBy);
        sb.append(", proposedTo=").append(proposedTo);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
