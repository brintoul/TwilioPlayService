/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.db.Customer;
import com.controlledthinking.dropwizard.db.MessageGroup;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.db.User;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
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

    public MessageGroupResource(MessageGroupDAO dao) {
        this.dao = dao;
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
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        dao.getById(groupId).getCustomerCollection().add(customer);
        return true;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("/user/{userId}")
    public List<MessageGroup> getGroupsForUser(@PathParam("userId") int userId) {
        return dao.getAllForUser(userId);
    }

}
