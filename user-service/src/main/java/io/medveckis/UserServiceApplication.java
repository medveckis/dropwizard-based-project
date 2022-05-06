package io.medveckis;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.medveckis.dao.UserDAO;
import io.medveckis.health.DatabaseHealthCheck;
import io.medveckis.model.Item;
import io.medveckis.model.User;
import io.medveckis.web.resource.UserResource;

public class UserServiceApplication extends Application<UserServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }

    private final HibernateBundle<UserServiceConfiguration> hibernateBundle =
            new HibernateBundle<>(User.class, Item.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(UserServiceConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    private final DiscoveryBundle<UserServiceConfiguration> discoveryBundle = new DiscoveryBundle<>() {

        @Override
        public DiscoveryFactory getDiscoveryFactory(UserServiceConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<UserServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(final UserServiceConfiguration configuration,
                    final Environment environment) {
        UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());
        DatabaseHealthCheck databaseHealthCheck = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(DatabaseHealthCheck.class, new Class[] {UserDAO.class}, new Object[]{userDAO});
        environment.healthChecks().register("db", databaseHealthCheck);
        environment.jersey().register(new UserResource(userDAO));
        environment.healthChecks().getHealthCheck("db").execute();
    }
}
