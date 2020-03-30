package io.squant.app.dao;

import java.util.List;

import io.squant.app.resource.User;

public interface UserDao {

    List<io.squant.app.dao.data.User> findAll();

    io.squant.app.dao.data.User findByExternalId(String externalId);

    io.squant.app.dao.data.User put(User user);

}
