package com.fri.code.users.api.v1.resources;

import com.fri.code.users.api.v1.dtos.ApiError;
import com.fri.code.users.lib.SubjectMetadata;
import com.fri.code.users.lib.UserMetadata;
import com.fri.code.users.services.beans.UserMetadataBean;
import com.kumuluz.ee.logs.cdi.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.eclipse.microprofile.metrics.annotation.Timed;


import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Log
@ApplicationScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserMetadataResource {

    @Inject
    private UserMetadataBean userMetadataBean;

    @GET
    @Operation(summary = "Get all users", description = "Returns details for the users.")
    @ApiResponses({
            @ApiResponse(description = "Users' details", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserMetadata.class))))
    })
    @Path("/all")
    @Timed
    public Response getUsers(){
        List<UserMetadata> users =userMetadataBean.getAllUsers();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Operation(summary = "Get user", description = "Returns details for specific user.")
    @ApiResponses({
            @ApiResponse(description = "User details", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    UserMetadata.class))),
            @ApiResponse(description = "The user cannot be found", responseCode = "404")
    })
    @Path("/{userID}")
    public Response getUserById(@PathParam("userID") Integer userID){
        try {
            UserMetadata user = userMetadataBean.getUserById(userID);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (Exception e) {
            ApiError error = createApiError("The user was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @GET
    @Operation(summary = "Get subjects for specific user", description = "Returns subject for specific user.")
    @ApiResponses({
                    @ApiResponse(description = "Subjects for user", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectMetadata.class)))),
            @ApiResponse(description = "The user details cannot be found", responseCode = "404")
    })
    @Path("/{userID}/subjects")
    public Response getSubjectsForUser(@PathParam("userID") Integer userID){
        try{
            List<SubjectMetadata> subjects = userMetadataBean.getSubjectsForUser(userID);
            return Response.status(Response.Status.OK).entity(subjects).build();
        }
        catch (Exception e) {
            ApiError error = createApiError(e.getMessage(), Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @PUT
    @Operation(summary = "Add subject ID for specific user", description = "Returns user with updated subject IDs.")
    @ApiResponses({
            @ApiResponse(description = "Add subject ID for user", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    UserMetadata.class))),
            @ApiResponse(description = "The subject ID cannot be added", responseCode = "400")
    })
    @RolesAllowed("admin")
    @Path("/{userID}/addSubject")
    public Response addUser(@PathParam("userID") Integer userID, @QueryParam("subjectID") Integer subjectID){
        UserMetadata userMetadata = userMetadataBean.addSubject(userID, subjectID);
        if (userMetadata == null){
            ApiError error = createApiError("Something is wrong", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(userMetadata).build();
    }

    @POST
    @Operation(summary = "Create a new user", description = "Returns the created user.")
    @ApiResponses({
            @ApiResponse(description = "Create new user", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    UserMetadata.class))),
            @ApiResponse(description = "The user cannot be created", responseCode = "400")
    })
    @RolesAllowed("admin")
    public Response createUser(UserMetadata userMetadata){

        if (userMetadata.getFirstName() == null || userMetadata.getLastName() == null || userMetadata.getEmail() == null) {
            ApiError error = createApiError("Some parameters are missing", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            try {
                userMetadata = userMetadataBean.createUserMetadata(userMetadata);
                return Response.status(Response.Status.OK).entity(userMetadata).build();
            } catch (Exception e) {
                ApiError error = createApiError("The user cannot be added", Response.Status.BAD_REQUEST);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

        }
    }

    @PUT
    @Operation(summary = "Update an existing user", description = "Returns the updated user.")
    @ApiResponses({
            @ApiResponse(description = "Update existing user", responseCode = "200", content = @Content(schema = @Schema(implementation =
                    UserMetadata.class))),
            @ApiResponse(description = "Missing parameters", responseCode = "400"),
            @ApiResponse(description = "The user cannot be found", responseCode = "404")
    })
    @RolesAllowed("admin")
    @Path("/{userID}")
    public Response putUser(@PathParam("userID") Integer userID, UserMetadata userMetadata){
        if (userMetadata.getFirstName() == null || userMetadata.getLastName() == null || userMetadata.getEmail() == null) {
            ApiError error = createApiError("Some parameters are missing", Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        userMetadata = userMetadataBean.putUserMetadata(userID, userMetadata);
        if (userMetadata == null) {
            ApiError error = createApiError("The user was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Response.Status.OK).entity(userMetadata).build();
    }

    @DELETE
    @Operation(summary = "Delete an existing user", description = "Deletes a specific user.")
    @ApiResponses({
            @ApiResponse(description = "Delete user", responseCode = "204"),
            @ApiResponse(description = "The user cannot be found", responseCode = "404")
    })
    @RolesAllowed("admin")
    @Path("/{userID}")
    public Response deleteUser(@PathParam("userID") Integer userID){
        if (userMetadataBean.deleteUserMetadata(userID)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            ApiError error = createApiError("The user was not found", Response.Status.NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    public ApiError createApiError(String message, Response.Status responseStatus){
        ApiError error = new ApiError();
        error.setCode(responseStatus.toString());
        error.setMessage(message);
        error.setStatus(responseStatus.getStatusCode());
        return error;
    }
}
