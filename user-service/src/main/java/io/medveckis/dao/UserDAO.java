package io.medveckis.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.medveckis.model.User;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> findUserById(Integer userId) {
        return Optional.ofNullable(get(userId));
    }

    public Integer save(User user) {
        return persist(user).getId();
    }
}
