package io.squant.app.web;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/1/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;

    public UserController(UserDao userDao) {

        this.userDao = userDao;
    }

    @GetMapping("/me")
    public User findMe(Principal principal) {

        io.squant.app.dao.data.User user = getOrAddUser(principal);
        return toDto(user);
    }

    @GetMapping
    public Page<User> findAll(Principal principal,
            @RequestParam(required = false, defaultValue = "0") int index,
            @RequestParam(required = false, defaultValue = "500") int size) {

        io.squant.app.dao.data.User user = getOrAddUser(principal);
        List<User> allUsers = userDao.findAll().stream()
                .filter(u -> u.getId() != user.getId())
                .sorted(Comparator.comparing(io.squant.app.dao.data.User::getName))
                .map(this::toDto)
                .collect(Collectors.toList());

        int count = allUsers.size() + 1;

        return new Page<>(allUsers, index, count);
    }

    private User toDto(io.squant.app.dao.data.User user) {
        return new User(user.getExternalId(), user.getName(), user.getEmail());
    }

    private io.squant.app.dao.data.User getOrAddUser(Principal principal) {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        OAuth2User oauthUser = token.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        User user = new User(null, name, email);

        return userDao.put(user);
    }
}
