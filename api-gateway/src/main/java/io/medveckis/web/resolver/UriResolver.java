package io.medveckis.web.resolver;

import io.dropwizard.discovery.client.DiscoveryClient;
import io.medveckis.ApiGatewayConfiguration;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;

public class UriResolver {
    private Map<DiscoveryClient, List<String>> routingMap;

    private final ApiGatewayConfiguration configuration;


    public UriResolver(ApiGatewayConfiguration configuration) {
        this.configuration = configuration;
        routingMap = Map.of(
                configuration.getBookDiscoveryClient(), List.of("books", "categories"),
                configuration.getUserDiscoveryClient(), List.of("users"),
                configuration.getBorrowManagementClient(), List.of("borrow-management")
        );
    }

    public String resolvePath(String path) throws Exception {
        DiscoveryClient client = routingMap
                .entrySet()
                .stream()
                .filter(entry -> hasMatch(entry.getValue(), path))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();

        ServiceInstance instance = client.getInstance();
        String address = instance.getAddress();
        Integer port = instance.getPort();
        String endpoint = "http://" + address + ":" + port + "/" + path;

        return endpoint;
    }

    private boolean hasMatch(List<String> matchers, String path) {
        return matchers.stream().anyMatch(path::contains);
    }
}
