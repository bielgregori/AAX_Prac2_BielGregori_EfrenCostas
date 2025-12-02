package aar.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import aar.models.User;
import aar.services.UserService;
import aar.repository.UserDaoImpl;

import java.util.List;

@Path("/users")
public class UserController {

    private final UserService userService;

    public UserController() {
        //Wiring manual
        this.userService = new UserService(new UserDaoImpl());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id " + id);
        }
        return user;
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response createUser(
            @FormParam("name") String name,
            @FormParam("profession") String profession) {

        User user = new User(name, profession);
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        User existing = userService.getUserById(id);
        if (existing == null) {
            throw new NotFoundException("User not found with id " + id);
        }

        existing.setName(user.getName());
        existing.setProfession(user.getProfession());

        userService.updateUser(existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User existing = userService.getUserById(id);
        if (existing == null) {
            throw new NotFoundException("User not found with id " + id);
        }

        userService.deleteUser(id);
        return Response.noContent().build();
    }
}

