/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.CustomerScheduledMessage;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.db.ScheduledMessageDAO;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brintoul
 */
@Path("/scheduledmessage")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduledMessageResource {
        
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CustomerDAO custDao;
    private final ScheduledMessageDAO dao;
    
    public ScheduledMessageResource(CustomerDAO custDao, ScheduledMessageDAO messageDao) {
        this.custDao = custDao;
        this.dao = messageDao;
    }
    
    @PUT
    @UnitOfWork
    @Timed
    @Path("/customer/{customerId}")
    public boolean setMessageForSending(@AuthRequired UserDTO user, @PathParam("customerId") int customerId, CustomerScheduledMessage message) {
        log.info("The message has the time: " + message.getTimeToSend());
        //TODO: HANDLE DB ERRORS HERE - PROBABLY IN LOTS OF PLACES, REALLY
        Customer theCustomer = custDao.getById(customerId);
        if( theCustomer.getUser().getUserId() != user.getUserId() ) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        message.setCustomer(custDao.getById(customerId));
        message.setSendingNumberText(user.getPhoneNumberText());
        dao.persist(message);
        return true;
    }


}
