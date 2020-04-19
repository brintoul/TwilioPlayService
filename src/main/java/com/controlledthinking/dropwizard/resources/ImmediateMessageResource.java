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
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.core.PhoneNumber;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.db.MessageDAO;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.services.QueueService;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author brintoul
 */
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class ImmediateMessageResource {

    private final QueueService queueService;
    private final MessageDAO dao;
    private final CustomerDAO custDao;
    private final MessageGroupDAO groupDao;
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public ImmediateMessageResource(QueueService qservice, MessageDAO dao, CustomerDAO custDao, MessageGroupDAO groupDao) {
        this.queueService = qservice;
        this.dao = dao;
        this.custDao = custDao;
        this.groupDao = groupDao;
    }
    
    private boolean doFullSend(CustomerImmediateMessage theMessage) {
        dao.persist(theMessage);
        List<CustomerImmediateMessage> listOfMessages = new ArrayList();
        listOfMessages.add(theMessage);
        //"sentSuccess" actually only means that it successfully made it to the 
        //queue - not guaranteed to have made it through Twilio API call
        boolean sentSuccess = queueService.sendMessageToQueue(listOfMessages);
        log.info("We are doing the full send to the queue.");
        if( sentSuccess )
            theMessage.setSent(true);
        return sentSuccess;        
    }
    
    @PUT
    @UnitOfWork
    @Timed
    @Path("/customer/{customerId}")
    public boolean sendMessageToCustomer(@AuthRequired UserDTO user, @PathParam("customerId") int customerId, CustomerImmediateMessage message) {
        //TODO: HANDLE DB ERRORS HERE - PROBABLY IN LOTS OF PLACES, REALLY
        Customer theCustomer = custDao.getById(customerId);
        if( theCustomer.getUser().getUserId() != user.getUserId() ) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        message.setCustomer(custDao.getById(customerId));
        message.setSendingNumberText(user.getPhoneNumberText());
        message.setOriginatingNumber(new PhoneNumber(1));
        return doFullSend(message);
    }

    @PUT
    @UnitOfWork
    @Timed
    @Path("/group/{groupId}")
    public boolean sendMessageToGroup(@AuthRequired UserDTO user, @PathParam("groupId") int groupId, CustomerImmediateMessage message) {
        boolean failHappened = false;
        MessageGroup group = groupDao.getWithCustomers(groupId);
        for(Customer customer : group.getCustomerCollection()) {
            message.setCustomer(customer);
            if(doFullSend(message) != true) {
                failHappened = true;
            }
        }
        return !failHappened;
    }
    
    @GET
    @UnitOfWork
    @Timed
    @Path("/receive")
    public boolean checkForMessages() {
        return true;
    }
}
