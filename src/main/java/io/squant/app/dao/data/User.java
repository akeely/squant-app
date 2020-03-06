package io.squant.app.dao.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;

public class User {

    private final int id;
    private final String externalId;
    private final String name;
    private final String email;

    public User(int id, String externalId, String name, String email) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id &&
                Objects.equals(externalId, user.externalId) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, name, email);
    }

    public static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return new User(resultSet.getInt("id"),
                    resultSet.getString("externalId"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));
        }
    }
}
