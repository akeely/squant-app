package io.squant.app.dao;

import java.util.List;

import io.squant.app.dao.data.Bet;

public interface BetDao {

    List<Bet> findAll(int index, int size);

    int countAll();

    List<Bet> findByUser(int userId, int index, int size);

    Bet findByExternalId(String externalId);

    int countByUser(int userId);

    void save(Bet bet);

    void setWinner(int id, int winnerId);

    void markPaid(int id);
}
