package io.squant.app.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.squant.app.dao.BetDao;
import io.squant.app.resource.Bet;
import io.squant.app.resource.Page;

@RestController
@RequestMapping("/api/1/bets")
public class BetController {

    private static final Logger LOG = LoggerFactory.getLogger(BetController.class);

    private final BetDao betDao;

    public BetController(BetDao betDao) {
        this.betDao = betDao;
    }

    @GetMapping
    public Page<Bet> findBets(Principal principal,
            @RequestParam(required = false, defaultValue = "false") boolean includePaid,
            @RequestParam(required = false, defaultValue = "0") int index,
            @RequestParam(required = false, defaultValue = "500") int size) {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        OAuth2User user = token.getPrincipal();
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        LOG.info("Retrieving bets for {} ({}).", name, email);

        return betDao.find(principal.getName(), includePaid, index, size);
    }
}
