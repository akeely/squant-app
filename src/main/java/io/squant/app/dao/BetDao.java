package io.squant.app.dao;

import java.util.List;

import io.squant.app.dao.data.Bet;

public interface BetDao {

    List<Bet> findByUser(int userId, int index, int size);

    int countByUser(int userId);

    void save(Bet bet);
}
