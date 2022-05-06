package io.medveckis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BorrowManagementServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    private DiscoveryFactory discovery = new DiscoveryFactory();
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    private DiscoveryClient userDiscoveryClient;

    private DiscoveryClient bookDiscoveryClient;

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

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
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
}
