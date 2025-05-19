package com.sismics.docs.rest.resource;

import com.sismics.docs.core.model.jpa.RegistrationRequest;
import com.sismics.docs.core.service.RegistrationRequestService;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.docs.rest.constant.BaseFunction;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Registration request REST resource.
 */
@Path("/registration-request")
public class RegistrationRequestResource extends BaseResource {

    /**
     * Submit a guest registration request.
     *
     * @api {post} /registration-request Submit registration request
     * @apiName PostRegistrationRequest
     * @apiGroup Registration
     * @apiParam {String} username Username
     * @apiParam {String} email Email
     * @apiParam {String} reason Reason for request
     * @apiSuccess {String} status OK
     * @apiError (client) ValidationError Required fields missing
     * @apiPermission none
     * @apiVersion 1.0.0
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submit(@FormParam("username") String username,
                           @FormParam("email") String email,
                           @FormParam("reason") String reason) {
        if (username == null || email == null) {
            throw new ClientException("ValidationError", "Username and email are required");
        }

        RegistrationRequestService service = new RegistrationRequestService(ThreadLocalContext.get().getEntityManager());
        service.submitRequest(username, email, reason);

        JsonObjectBuilder response = Json.createObjectBuilder().add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    private boolean isAdmin() {
        return hasBaseFunction(BaseFunction.ADMIN);
    }

    /**
     * List all pending registration requests.
     */
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingRequests() {
        if (!authenticate() || !isAdmin()) {
            throw new ForbiddenClientException();
        }

        RegistrationRequestService service = new RegistrationRequestService(ThreadLocalContext.get().getEntityManager());
        List<RegistrationRequest> list = service.getPendingRequests();

        JsonArrayBuilder array = Json.createArrayBuilder();
        for (RegistrationRequest req : list) {
            array.add(Json.createObjectBuilder()
                    .add("id", req.getId())
                    .add("username", req.getUsername())
                    .add("email", req.getEmail())
                    .add("reason", req.getReason() == null ? "" : req.getReason())
                    .add("createDate", req.getCreateDate().getTime()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder().add("requests", array);
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Approve a registration request by ID.
     */
    @POST
    @Path("{id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approve(@PathParam("id") String id) {
        if (!authenticate() || !isAdmin()) {
            throw new ForbiddenClientException();
        }

        RegistrationRequestService service = new RegistrationRequestService(ThreadLocalContext.get().getEntityManager());
        boolean success = service.approveRequest(id);
        if (!success) {
            throw new ClientException("ApprovalFailed", "Failed to approve request");
        }

        JsonObjectBuilder response = Json.createObjectBuilder().add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Reject a registration request by ID.
     */
    @POST
    @Path("{id}/reject")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reject(@PathParam("id") String id) {
        if (!authenticate() || !isAdmin()) {
            throw new ForbiddenClientException();
        }

        RegistrationRequestService service = new RegistrationRequestService(ThreadLocalContext.get().getEntityManager());
        boolean success = service.rejectRequest(id);
        if (!success) {
            throw new ClientException("RejectionFailed", "Failed to reject request");
        }

        JsonObjectBuilder response = Json.createObjectBuilder().add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}
