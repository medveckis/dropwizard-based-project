package io.medveckis.health;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.dao.UserDAO;
import io.medveckis.model.Role;
import io.medveckis.model.User;

import java.util.stream.IntStream;

public class DatabaseHealthCheck extends HealthCheck {
    private final UserDAO userDAO;

    public DatabaseHealthCheck(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @UnitOfWork
    protected Result check() throws Exception {
        dataLoaderForUsers();
        return Result.healthy();
    }

    private void dataLoaderForUsers() {
        IntStream.range(0, 4).forEach(idx -> userDAO.save(createTestUser(idx)));
    }

    private User createTestUser(int idx) {
        String firstName = "firstName_" + idx;
        String lastName = "lastName_" + idx;
        int age = idx + 1;
        String email = "example" + idx + "@mail.com";
        return new User(firstName, lastName, age, email, 2, age == 4 ? Role.ADMIN : Role.CUSTOMER);
    }
}
