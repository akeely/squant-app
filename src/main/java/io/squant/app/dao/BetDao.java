package io.squant.app.dao;

import io.squant.app.resource.Bet;
import io.squant.app.resource.Page;

public interface BetDao {

    Page<Bet> find(String userId, boolean includePaid, int index, int size);
}
