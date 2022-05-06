package io.medveckis.web.resource;

import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.dao.UserDAO;
import io.medveckis.model.Role;
import io.medveckis.model.User;
import io.medveckis.web.dto.UserData;
import io.medveckis.web.form.UserForm;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path(value = "/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response createUser(UserForm userForm, @Context UriInfo uriInfo) {
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(userDAO.save(convert(convert(userForm)))));
        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Path(value = "/{userId}")
    @UnitOfWork
    public UserData createUser(@PathParam(value = "userId") Integer userId) {
        return userDAO.findUserById(userId)
                .map(user -> new UserData(user.getId(), user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(), user.getLoyaltyLevel(), user.getRole()))
                .orElse(new UserData());
    }

    private UserData convert(UserForm userForm) {
        UserData userData = new UserData();
        userData.setFirstName(userForm.getFirstName());
        userData.setLastName(userForm.getLastName());
        userData.setEmail(userForm.getEmail());
        userData.setAge(userForm.getAge());
        userData.setRole(Role.CUSTOMER);
        return userData;
    }

    private User convert(UserData userData) {
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setAge(userData.getAge());
        user.setRole(userData.getRole());
        user.setLoyaltyLevel(1);
        return user;
    }
}
