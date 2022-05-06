package io.medveckis;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;
import io.dropwizard.discovery.client.DiscoveryClientManager;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.medveckis.web.resolver.UriResolver;
import io.medveckis.web.resources.ApiGatewayResource;

import javax.ws.rs.client.Client;

public class ApiGatewayApplication extends Application<ApiGatewayConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ApiGatewayApplication().run(args);
    }

    private final DiscoveryBundle<ApiGatewayConfiguration> discoveryBundle = new DiscoveryBundle<>() {

        @Override
        public DiscoveryFactory getDiscoveryFactory(ApiGatewayConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }
    };


    @Override
    public void initialize(final Bootstrap<ApiGatewayConfiguration> bootstrap) {
        bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(final ApiGatewayConfiguration configuration,
                    final Environment environment) {
        final DiscoveryClient userClient = discoveryBundle.newDiscoveryClient("user-service");
        DiscoveryClientManager userManager = new DiscoveryClientManager(userClient);
        environment.lifecycle().manage(userManager);
        configuration.setUserDiscoveryClient(userClient);

        final DiscoveryClient bookClient = discoveryBundle.newDiscoveryClient("book-service");
        DiscoveryClientManager bookManager = new DiscoveryClientManager(bookClient);
        environment.lifecycle().manage(bookManager);
        configuration.setBookDiscoveryClient(bookClient);

        final DiscoveryClient borrowManagementClient = discoveryBundle.newDiscoveryClient("borrow-management-service");
        DiscoveryClientManager borrowManagementManager = new DiscoveryClientManager(borrowManagementClient);
        environment.lifecycle().manage(borrowManagementManager);
        configuration.setBorrowManagementClient(borrowManagementClient);

        UriResolver uriResolver = new UriResolver(configuration);

        final Client jerseyClient = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build("jerseyClient");

        environment.jersey().register(new ApiGatewayResource(uriResolver, jerseyClient));
    }
}
