package com.schremser.challenger.services;

import com.google.gson.JsonObject;
import com.schremser.challenger.app.ProviderRegistry;
import com.schremser.challenger.domains.ExpenseCreationInfo;
import com.schremser.challenger.domains.ExpenseInfo;
import com.schremser.challenger.domains.ExpenseType;
import com.schremser.challenger.providers.IExpenseProvider;
import com.schremser.challenger.providers.RequestProcessingException;
import com.schremser.challenger.providers.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Path("/signup")
public class SignupResource {
    private final static Logger log = LoggerFactory.getLogger(SignupResource.class);

    @Context
    private transient HttpServletRequest i_request;

    public SignupResource() {
    }

    @POST()
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject signup(@FormParam("email") String email, @FormParam("password") String password) {
        JsonObject json = new JsonObject();
        json.addProperty("name", "maxi");
        json.addProperty("email", email);
        return json;
    }

}
