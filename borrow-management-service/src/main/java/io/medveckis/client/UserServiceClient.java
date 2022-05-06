package io.medveckis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mtakaki.dropwizard.circuitbreaker.CircuitBreakerManager;
import com.github.mtakaki.dropwizard.circuitbreaker.OperationException;
import io.medveckis.BorrowManagementServiceConfiguration;
import io.medveckis.client.response.UserResponse;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);
    private final static String SERVICE_PATH = "/users/";

    private final Client client;
    private final BorrowManagementServiceConfiguration configuration;
    private final CircuitBreakerManager circuitBreakerManager;
    private final ObjectMapper objectMapper;

    public UserServiceClient(Client client, BorrowManagementServiceConfiguration configuration, CircuitBreakerManager circuitBreakerManager, ObjectMapper objectMapper) {
        this.client = client;
        this.configuration = configuration;
        this.circuitBreakerManager = circuitBreakerManager;
        this.objectMapper = objectMapper;
    }

    public UserResponse getUserById(Integer userId) {
        try {
            ClientContext clientContext = new ClientContext();
            circuitBreakerManager.wrapCodeBlockWithCircuitBreaker("userClient", meter -> {
                try {
                    ServiceInstance instance = this.configuration.getUserDiscoveryClient().getInstance();
                    String address = instance.getAddress();
                    Integer port = instance.getPort();
                    String endpoint = "http://" + address + ":" + port + SERVICE_PATH + userId;
                    Response response = client.target(endpoint).request(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON).get();
                    String responseString = response.readEntity(String.class);
                    clientContext.setUserResponse(objectMapper.readValue(responseString, UserResponse.class));
                } catch (Exception e) {
                    throw new OperationException(e);
                }
            });
            return clientContext.getUserResponse();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new UserResponse(userId, "Unknown", "Unknown", -1, "Unknown", 0, "Unknown");
    }

    private class ClientContext {
        private UserResponse userResponse;

        public UserResponse getUserResponse() {
            return userResponse;
        }

        public void setUserResponse(UserResponse userResponse) {
            this.userResponse = userResponse;
        }
    }

//    public boolean isClientHealthy() {
//        try {
//            ServiceInstance instance = this.configuration.getProductCatalogDiscoveryClient().getInstance();
//            String address = instance.getAddress();
//            Integer port = instance.getPort();
//            String endpoint = "http://" + address + ":" + port + HEALTH_CHECK_PATH;
//            Response response = client.target(endpoint).request(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON).get();
//            return response.getStatus() == 200;
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//        return false;
//    }
}
