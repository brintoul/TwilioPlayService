/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.core.User;
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
import javax.ws.rs.core.MediaType;

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
    public boolean createGroup(MessageGroup group) {
        //TODO:  use real user
        User user = new User();
        user.setUserId(1);
        group.setUser(user);
        dao.persist(group);
        return true;
    }
    
    @POST
    @Timed
    @UnitOfWork
    @Path("{groupId}/customers")
    public boolean addCustomerToGroup(@PathParam("groupId") int groupId, @QueryParam("customerId") int customerId) {
        //Customer customer = custDao.getById(customerId);
        MessageGroup mg = dao.getWithCustomers(groupId);
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        mg.getCustomerCollection().add(customer);
        return true;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("/user/{userId}")
    public List<MessageGroup> getGroupsForUser(@PathParam("userId") int userId) {
        return dao.getAllForUser(userId);
    }
    
    @DELETE
    @Timed
    @UnitOfWork
    @Path("{groupId}/customers/{customerId}")
    public boolean removeCustomerFromGroup(@PathParam("groupId") int groupId, @PathParam("customerId") int customerId) {
        return dao.removeCustomerFromGroup(customerId, groupId);
    }

}
