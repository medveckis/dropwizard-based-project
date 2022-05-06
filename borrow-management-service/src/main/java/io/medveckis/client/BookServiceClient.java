package io.medveckis.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mtakaki.dropwizard.circuitbreaker.CircuitBreakerManager;
import com.github.mtakaki.dropwizard.circuitbreaker.OperationException;
import io.medveckis.BorrowManagementServiceConfiguration;
import io.medveckis.client.response.BookResponse;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BookServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceClient.class);
    private final static String SERVICE_PATH = "/books/";

    private final Client client;
    private final BorrowManagementServiceConfiguration configuration;
    private final CircuitBreakerManager circuitBreakerManager;
    private final ObjectMapper objectMapper;

    public BookServiceClient(Client client, BorrowManagementServiceConfiguration configuration, CircuitBreakerManager circuitBreakerManager, ObjectMapper objectMapper) {
        this.client = client;
        this.configuration = configuration;
        this.circuitBreakerManager = circuitBreakerManager;
        this.objectMapper = objectMapper;
    }

    public BookResponse getBookById(Integer bookId) {
        try {
            ClientContext clientContext = new ClientContext();
            circuitBreakerManager.wrapCodeBlockWithCircuitBreaker("bookClient", meter -> {
                try {
                    ServiceInstance instance = this.configuration.getBookDiscoveryClient().getInstance();
                    String address = instance.getAddress();
                    Integer port = instance.getPort();
                    String endpoint = "http://" + address + ":" + port + SERVICE_PATH + bookId;
                    Response response = client.target(endpoint).request(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON).get();
                    String responseString = response.readEntity(String.class);
                    clientContext.setBookResponse(objectMapper.readValue(responseString, BookResponse.class));
                } catch (Exception e) {
                    throw new OperationException(e);
                }
            });
            return clientContext.getBookResponse();

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return new BookResponse(bookId, -1., "Unknown name", -1);
    }

    private class ClientContext {
        private BookResponse bookResponse;

        public BookResponse getBookResponse() {
            return bookResponse;
        }

        public void setBookResponse(BookResponse bookResponse) {
            this.bookResponse = bookResponse;
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
