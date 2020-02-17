package io.squant.app.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.squant.app.dao.BetDao;
import io.squant.app.dao.UserDao;
import io.squant.app.resource.Bet;
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
            @RequestParam(required = false, defaultValue = "false") boolean includePaid,
            @RequestParam(required = false, defaultValue = "0") int index,
            @RequestParam(required = false, defaultValue = "500") int size) {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        OAuth2User oauthUser = token.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        User user = new User(name, email);
        LOG.info("Retrieving bets for {}.", user);

        int userId = userDao.put(user);
        return betDao.find(userId, includePaid, index, size);
    }
}
