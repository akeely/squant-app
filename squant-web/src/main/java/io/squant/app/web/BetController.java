package io.squant.app.web;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.squant.core.bet.Bet;
import io.squant.core.bet.BetRepository;

@RestController
@RequestMapping("/{groupId}/bet")
public class BetController {

    private final BetRepository betRepository;

    public BetController(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @GetMapping
    public List<Bet> findAll() {
        return betRepository.findAll("userId");
    }

    @GetMapping("/{betId}")
    public Bet findOne(@PathVariable String betId) {

        return betRepository.findOne(betId);
    }

    @PostMapping
    public void create(@RequestBody Bet bet) {

        betRepository.save(bet);
    }

    @PutMapping("/{betId}")
    public void update(@PathVariable("betId") String betId, @RequestBody Bet bet) {

        checkArgument(bet.getId().equals(betId), "betId in body must match path segment.");

        betRepository.save(bet);
    }
}
