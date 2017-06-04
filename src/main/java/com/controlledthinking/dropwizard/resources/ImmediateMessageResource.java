/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.CustomerImmediateMessage;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.MessageDAO;
import com.controlledthinking.dropwizard.services.QueueService;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author brintoul
 */
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class ImmediateMessageResource {

    private QueueService queueService;
    private MessageDAO dao;
    
    public ImmediateMessageResource(QueueService qservice, MessageDAO dao) {
        this.queueService = qservice;
        this.dao = dao;
    }
    
    @PUT
    @UnitOfWork
    @Timed
    @Path("/customer/{customerId}")
    public boolean sendMessageToCustomer(@AuthRequired UserDTO user, @PathParam("customerId") int customerId, CustomerImmediateMessage message) {
        message.setCustomer(new Customer(customerId));
        dao.persist(message);
        return true;
    }
}
