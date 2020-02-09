package io.squant.core.bet;

import java.util.List;

public interface BetRepository {

    List<Bet> findAll(String userId);
    Bet findOne(String betId);
    void save(Bet bet);
}
