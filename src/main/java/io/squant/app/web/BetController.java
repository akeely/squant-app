package io.squant.app.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.squant.app.dao.BetDao;
import io.squant.app.dao.UserDao;
import io.squant.app.resource.Bet;
import io.squant.app.resource.BetRequest;
import io.squant.app.resource.Page;
import io.squant.app.resource.User;

@RestController
@RequestMapping("/api/1/bets")
public class BetController {

    private static final Logger LOG = LoggerFactory.getLogger(BetController.class);

    private final BetDao betDao;
    private final UserDao userDao;

    public BetController(BetDao betDao, UserDao userDao) {
        this.betDao = betDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Page<Bet> findBets(Principal principal,
            @RequestParam(required = false, defaultValue = "0") int index,
            @RequestParam(required = false, defaultValue = "500") int size) {

        io.squant.app.dao.data.User user = getOrAddUser(principal);
        List<Bet> bets = betDao.findByUser(user.getId(), index, size).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        int count = betDao.countByUser(user.getId());

        return new Page<>(bets, index, count);
    }

    private Bet toDto(io.squant.app.dao.data.Bet bet) {

        Map<Integer, User> users = userDao.findAll().stream()
                .collect(Collectors.toMap(io.squant.app.dao.data.User::getId, this::toDto));

        return new Bet(users.get(bet.getCreator()),
                users.get(bet.getAgainst()),
                bet.getWinner() == null ? null : users.get(bet.getWinner()),
                bet.getAmount(),
                bet.getCurrency(),
                bet.getDescription(),
                bet.isPaid());
    }

    private User toDto(io.squant.app.dao.data.User user) {
        return new User(user.getExternalId(), user.getName(), user.getEmail());
    }

    @PostMapping
    public void addBet(Principal principal, @RequestBody BetRequest bet) {

        io.squant.app.dao.data.User creator = getOrAddUser(principal);
        betDao.save(toBet(creator.getId(), bet));
    }

    private io.squant.app.dao.data.Bet toBet(int creatorId, BetRequest bet) {

        io.squant.app.dao.data.User against = userDao.findByExternalId(bet.getAgainst());

        return new io.squant.app.dao.data.Bet(
                creatorId,
                against.getId(),
                bet.getAmount(),
                bet.getCurrency(),
                bet.getDescription());
    }

    private io.squant.app.dao.data.User getOrAddUser(Principal principal) {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        OAuth2User oauthUser = token.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        User user = new User(null, name, email);
        LOG.info("Retrieving bets for {}.", user);

        return userDao.put(user);
    }
}
