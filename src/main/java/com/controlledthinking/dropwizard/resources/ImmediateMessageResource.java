/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.core.CustomerImmediateMessage;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.CustomerDAO;
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
    private CustomerDAO custDao;
    
    public ImmediateMessageResource(QueueService qservice, MessageDAO dao, CustomerDAO custDao) {
        this.queueService = qservice;
        this.dao = dao;
        this.custDao = custDao;
    }
    
    @PUT
    @UnitOfWork
    @Timed
    @Path("/customer/{customerId}")
    public boolean sendMessageToCustomer(@AuthRequired UserDTO user, @PathParam("customerId") int customerId, CustomerImmediateMessage message) {
        //TODO: HANDLE DB ERRORS HERE - PROBABLY IN LOTS OF PLACES, REALLY
        message.setCustomer(custDao.getById(customerId));
        dao.persist(message);
        boolean sentSuccess = queueService.sendMessageToQueue(message);
        //TODO: ADD 'SENT' BIT TO MESSAGE TABLE
        return sentSuccess;
    }
}
