/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.core.UserDTO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author brintoul
 */
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class MessageGroupResource {
    
    private final MessageGroupDAO dao;
    private final CustomerDAO custDao;

    public MessageGroupResource(MessageGroupDAO dao, CustomerDAO custDao) {
        this.dao = dao;
        this.custDao = custDao;
    }
    
    @PUT
    @Timed
    @UnitOfWork
    public boolean createGroup(@AuthRequired(Privilege.USER) UserDTO user, MessageGroup group) {
        group.setUser(new User(user.getUserId()));
        dao.persist(group);
        return true;
    }
    
    @POST
    @Timed
    @UnitOfWork
    @Path("{groupId}/customers")
    public boolean addCustomerToGroup(@AuthRequired(Privilege.USER) UserDTO user, @PathParam("groupId") int groupId, @QueryParam("customerId") int customerId) {
        MessageGroup mg = dao.getWithCustomers(groupId);
        Customer customer = custDao.getById(customerId);
        if( customer == null ) {
            return false;
        }
        if( customer.getUser().getUserId() != user.getUserId()) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        mg.getCustomerCollection().add(customer);
        return true;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("/user")
    public List<MessageGroup> getGroupsForUser(@AuthRequired(Privilege.USER) UserDTO user) {
        return dao.getAllForUser(user.getUserId());
    }
    
    @DELETE
    @Timed
    @UnitOfWork
    @Path("{groupId}/customers/{customerId}")
    public boolean removeCustomerFromGroup(@AuthRequired(Privilege.USER) UserDTO user, @PathParam("groupId") int groupId, @PathParam("customerId") int customerId) {
        return dao.removeCustomerFromGroup(customerId, groupId);
    }
    
    @DELETE
    @Timed
    @UnitOfWork
    @Path("{groupId}")
    public boolean deleteGroup(@AuthRequired(Privilege.USER) UserDTO user, @PathParam("groupId") int groupId) {
        MessageGroup theGroup = dao.getById(groupId);
        if(theGroup.getUser().getUserId() != user.getUserId()) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        return dao.deleteGroup(groupId);
    }

}
