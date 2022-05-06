package io.medveckis;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.github.mtakaki.dropwizard.circuitbreaker.CircuitBreakerManager;
import com.github.mtakaki.dropwizard.circuitbreaker.RateType;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;
import io.dropwizard.discovery.client.DiscoveryClientManager;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.medveckis.client.BookServiceClient;
import io.medveckis.client.UserServiceClient;
import io.medveckis.dao.BorrowManagementDAO;
import io.medveckis.model.BookRecord;
import io.medveckis.model.Item;
import io.medveckis.web.resources.BorrowManagementResource;

import javax.ws.rs.client.Client;

public class BorrowManagementServiceApplication extends Application<BorrowManagementServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BorrowManagementServiceApplication().run(args);
    }

    private final HibernateBundle<BorrowManagementServiceConfiguration> hibernateBundle =
            new HibernateBundle<>(BookRecord.class, Item.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(BorrowManagementServiceConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    private final DiscoveryBundle<BorrowManagementServiceConfiguration> discoveryBundle = new DiscoveryBundle<>() {

        @Override
        public DiscoveryFactory getDiscoveryFactory(BorrowManagementServiceConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<BorrowManagementServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(final BorrowManagementServiceConfiguration configuration, final Environment environment) {
        final DiscoveryClient userClient = discoveryBundle.newDiscoveryClient("user-service");
        DiscoveryClientManager userManager = new DiscoveryClientManager(userClient);
        environment.lifecycle().manage(userManager);
        configuration.setUserDiscoveryClient(userClient);

        final DiscoveryClient bookClient = discoveryBundle.newDiscoveryClient("book-service");
        DiscoveryClientManager bookManager = new DiscoveryClientManager(bookClient);
        environment.lifecycle().manage(bookManager);
        configuration.setBookDiscoveryClient(bookClient);

        final Client jerseyClient = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build("jerseyClient");

        environment.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        CircuitBreakerManager userCircuitBreaker = new CircuitBreakerManager(new MetricRegistry(), 0.5, RateType.ONE_MINUTE);
        UserServiceClient userServiceClient = new UserServiceClient(jerseyClient, configuration, userCircuitBreaker, environment.getObjectMapper());

        CircuitBreakerManager bookCircuitBreaker = new CircuitBreakerManager(new MetricRegistry(), 0.5, RateType.ONE_MINUTE);
        BookServiceClient bookServiceClient = new BookServiceClient(jerseyClient, configuration, bookCircuitBreaker, environment.getObjectMapper());

        BorrowManagementDAO borrowManagementDAO = new BorrowManagementDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new BorrowManagementResource(borrowManagementDAO, userServiceClient, bookServiceClient));
    }

}
