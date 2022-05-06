package io.medveckis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApiGatewayConfiguration extends Configuration {
    @Valid
    @NotNull
    private DiscoveryFactory discovery = new DiscoveryFactory();

    private DiscoveryClient userDiscoveryClient;
    private DiscoveryClient bookDiscoveryClient;
    private DiscoveryClient borrowManagementClient;

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("discovery")
    public DiscoveryFactory getDiscoveryFactory() {
        return discovery;
    }

    @JsonProperty("discovery")
    public void setDiscoveryFactory(DiscoveryFactory discoveryFactory) {
        this.discovery = discoveryFactory;
    }

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty("jerseyClient")
    public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
        this.jerseyClient = jerseyClient;
    }

    public DiscoveryClient getUserDiscoveryClient() {
        return userDiscoveryClient;
    }

    public void setUserDiscoveryClient(DiscoveryClient userDiscoveryClient) {
        this.userDiscoveryClient = userDiscoveryClient;
    }

    public DiscoveryClient getBookDiscoveryClient() {
        return bookDiscoveryClient;
    }

    public void setBookDiscoveryClient(DiscoveryClient bookDiscoveryClient) {
        this.bookDiscoveryClient = bookDiscoveryClient;
    }

    public DiscoveryClient getBorrowManagementClient() {
        return borrowManagementClient;
    }

    public void setBorrowManagementClient(DiscoveryClient borrowManagementClient) {
        this.borrowManagementClient = borrowManagementClient;
    }
}
