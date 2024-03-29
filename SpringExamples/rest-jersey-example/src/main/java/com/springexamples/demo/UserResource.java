package com.springexamples.demo;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import com.springexamples.demo.dao.UserDB;
import com.springexamples.demo.model.User;
import com.springexamples.demo.model.Users;
 
@Path("/users")
public class UserResource
{
    @GET
    @Produces("application/json")
    public Users getAllUsers() {
        Users users = new Users();
        users.setUsers(UserDB.getUsers());
        return users;
    }
     
    @POST
    @Consumes("application/json")
    public Response createUser(User user) throws URISyntaxException
    {
        if(user.getFirstName() == null || user.getLastName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        user.setId(UserDB.getUsers().size()+1);
        UserDB.addUser(user);
        return Response.status(201).contentLocation(new URI("/users/"+user.getId())).build();
    }
 
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") int id) throws URISyntaxException
    {
        User user = UserDB.getUserById(id);
        if(user == null) {
            return Response.status(404).build();
        }
        return Response
                .status(200)
                .entity(user)
                .contentLocation(new URI("/user-management/"+id)).build();
    }
 
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateUser(@PathParam("id") int id, User user) throws URISyntaxException
    {
        User temp = UserDB.getUserById(id);
        if(user == null) {
            return Response.status(404).build();
        }
        temp.setFirstName(user.getFirstName());
        temp.setLastName(user.getLastName());
        UserDB.updateUser(temp);
        return Response.status(200).entity(temp).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) throws URISyntaxException {
        User user = UserDB.getUserById(id);
        if(user != null) {
        	UserDB.removeUser(user.getId());
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
}
