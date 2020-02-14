package io.squant.app.dao;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.squant.app.resource.Bet;
import io.squant.app.resource.Page;

@Repository
public class BetDaoImpl implements BetDao {

    private static final Logger LOG = LoggerFactory.getLogger(BetDaoImpl.class);

    @Override
    public Page<Bet> find(String userId, boolean includePaid, int index, int size) {

        LOG.info("Getting bets for {} (include paid: {}). Page idx: {}, size: {}.", userId, includePaid, index, size);
        return new Page<>(List.of(new Bet(
                "Andrew",
                "Jake",
                BigDecimal.ONE,
                "beers",
                "Test bet",
                false
        )), 0, 1);
    }
}
