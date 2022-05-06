package io.medveckis.web.resources;

import io.medveckis.web.resolver.UriResolver;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path(value = "/")
@Produces(MediaType.APPLICATION_JSON)
public class ApiGatewayResource {
    private final UriResolver uriResolver;
    private final Client client;

    public ApiGatewayResource(UriResolver uriResolver, Client client) {
        this.uriResolver = uriResolver;
        this.client = client;
    }

    @GET
    @Path("{path:.+}")
    public Response get(@Context UriInfo uriInfo) throws Exception {
        return processRequest(uriInfo, "get", "");
    }

    @POST
    @Path("{path:.+}")
    public Response post(String requestBody, @Context UriInfo uriInfo) throws Exception {
        return processRequest(uriInfo, "post", requestBody);
    }

    private Response processRequest(UriInfo uriInfo, String method, String requestBody) throws Exception {
        String endpointToCall = uriResolver.resolvePath(
                uriInfo.getRequestUri().toString().replace(uriInfo.getBaseUri().toString(), "")
        );
//        return Response.temporaryRedirect(URI.create(endpointToCall)).build();

        if (method.equals("get")) {
            return client.target(endpointToCall).request().get();
        } else if (method.equals("post")) {
            return client.target(endpointToCall).request().post(Entity.json(requestBody), Response.class);
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
