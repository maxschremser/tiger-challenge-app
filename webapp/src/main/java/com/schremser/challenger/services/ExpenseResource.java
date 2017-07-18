package com.schremser.challenger.services;

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


@Path("/expenses")
public class ExpenseResource {
    private final static Logger log = LoggerFactory.getLogger(ExpenseResource.class);
    public static final String PROP_EXPENSE_ID = "expenseId";

    @Context
    private transient HttpServletRequest i_request;

    private IExpenseProvider i_provider;

    public ExpenseResource() {
        i_provider = ProviderRegistry.instance().getExpenseProvider();
    }

    protected String getLoginSessionId() {
        // return (String) i_request.getAttribute( LoginService.PROP_ALGO_SESSION_ID );
        return "abcdef123456";
    }

    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExpenseInfo createDataset(ExpenseCreationInfo datasetInfo) {
        String loginSessionId = getLoginSessionId();
        return i_provider.createExpense(loginSessionId, datasetInfo);
    }

    @PUT()
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ExpenseInfo updateDataset(@PathParam("id") String id, ExpenseInfo datasetInfo) {
        String loginSessionId = getLoginSessionId();
        log.debug("Updating expense [" + id + "]");
        try {
            ExpenseInfo updatedDI = i_provider.updateExpense(loginSessionId, id, datasetInfo);
            return updatedDI;
        } catch (ResourceNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Expense info not valid: " + e.getMessage());
        } catch (RequestProcessingException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        } catch (Exception e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ExpenseInfo> getExpenses(@QueryParam("type") String type) {
        String loginSessionId = getLoginSessionId();
        log.debug("Requesting expenses " + (type != null ? " type=" + type : ""));
        if (type != null) {
            try {
                ExpenseType expenseType = ExpenseType.valueOf(type);
                return i_provider.getExpensesByType(loginSessionId, expenseType);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Requested expense type [" + type + "] is not valid");
            } catch (RequestProcessingException e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            } catch (Exception e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        } else {
            try {
                Collection<ExpenseInfo> result = i_provider.getExpenses(loginSessionId);
                log.debug("ExpenseResource.getExpenses: returning " + result.size() + " entries");
                return result;
            } catch (ResourceNotFoundException e) {
                throw new NotFoundException(e.getMessage(), e);
            } catch (RequestProcessingException e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            } catch (Exception e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        }
    }

    @GET()
    @Path("/byDate/today")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ExpenseInfo> getTodaysExpenses(@QueryParam("type") String type) {
        String loginSessionId = getLoginSessionId();
        log.debug("Requesting expenses " + (type != null ? " type=" + type : ""));
        if (type != null) {
            try {
                ExpenseType expenseType = ExpenseType.valueOf(type);
                return i_provider.getTodaysExpensesByType(loginSessionId, expenseType);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Requested expense type [" + type + "] is not valid");
            } catch (RequestProcessingException e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            } catch (Exception e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        } else {
            try {
                Collection<ExpenseInfo> result = i_provider.getTodaysExpenses(loginSessionId);
                log.debug("ExpenseResource.getTodaysExpenses: returning " + result.size() + " entries");
                return result;
            } catch (ResourceNotFoundException e) {
                throw new NotFoundException(e.getMessage(), e);
            } catch (RequestProcessingException e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            } catch (Exception e) {
                throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        }
    }

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ExpenseInfo getExpense(@PathParam("id") String id) {
        try {
            String loginSessionId = getLoginSessionId();
            ExpenseInfo di = i_provider.getExpense(loginSessionId, id);
            return di;
        } catch (ResourceNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (RequestProcessingException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        } catch (Exception e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteExpense(@PathParam("id") String id) {
        try {
            i_provider.deleteExpense(getLoginSessionId(), id);
        } catch (ResourceNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (RequestProcessingException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        } catch (Exception e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

}
