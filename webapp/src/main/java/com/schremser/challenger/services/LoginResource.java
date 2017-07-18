package com.schremser.challenger.services;

import com.google.gson.JsonObject;
import com.schremser.challenger.domains.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/login")
public class LoginResource {
    private final static Logger log = LoggerFactory.getLogger(LoginResource.class);

    @Context
    private transient HttpServletRequest i_request;

    public LoginResource() {
    }

    @POST()
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject login(@FormParam("email") String email, @FormParam("password") String password) {
        JsonObject json = new JsonObject();
        json.addProperty("name", "maxi");
        json.addProperty("email", email);
        return json;
    }

    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject login(User user) {
        JsonObject json = new JsonObject();
        json.addProperty("name", "maxi");
        json.addProperty("email", user.email);
        return json;
    }

}
