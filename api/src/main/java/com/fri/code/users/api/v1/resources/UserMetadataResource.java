package com.fri.code.users.api.v1.resources;

import com.fri.code.users.api.v1.dtos.ApiError;
import com.fri.code.users.lib.UserMetadata;
import com.fri.code.users.services.beans.UserMetadataBean;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserMetadataResource {

    @Inject
    private UserMetadataBean userMetadataBean;

    @GET
    @Path("/all")
    @Timed
    public Response getUsers(){
        List<UserMetadata> users =userMetadataBean.getAllUsers();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("/{userID}")
    public Response getUserById(@PathParam("userID") Integer userID){
        try {
            UserMetadata user = userMetadataBean.getUserById(userID);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (Exception e) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The user was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @POST
    public Response createUser(UserMetadata userMetadata){

        if (userMetadata.getFirstName() == null || userMetadata.getLastName() == null || userMetadata.getEmail() == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.BAD_REQUEST.toString());
            error.setMessage("Some parameters are missing");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            try {
                userMetadata = userMetadataBean.createUserMetadata(userMetadata);
                return Response.status(Response.Status.OK).entity(userMetadata).build();
            } catch (Exception e) {
                ApiError error = new ApiError();
                error.setCode(Response.Status.BAD_REQUEST.toString());
                error.setMessage("The user cannot be added");
                error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

        }
    }

    @PUT
    @Path("/{userID}")
    public Response putUser(@PathParam("userID") Integer userID, UserMetadata userMetadata){
        if (userMetadata.getFirstName() == null || userMetadata.getLastName() == null || userMetadata.getEmail() == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.BAD_REQUEST.toString());
            error.setMessage("Some parameters are missing");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        userMetadata = userMetadataBean.putUserMetadata(userID, userMetadata);
        if (userMetadata == null) {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The user was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(userMetadata).build();
    }

    @DELETE
    @Path("/{userID}")
    public Response deleteUser(@PathParam("userID") Integer userID){
        if (userMetadataBean.deleteUserMetadata(userID)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            ApiError error = new ApiError();
            error.setCode(Response.Status.NOT_FOUND.toString());
            error.setMessage("The user was not found");
            error.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }
}
