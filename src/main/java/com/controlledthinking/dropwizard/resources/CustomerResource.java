/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.db.Customer;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.db.User;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author brintoul
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerDAO dao;

    public CustomerResource(CustomerDAO dao) {
        this.dao = dao;
    }

    @PUT
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCustomer(Customer customer) {
        //TODO:  use real user
        User user = new User();
        user.setUserId(1);
        customer.setUser(user);
        dao.create(customer);
        return "created";
    }
     

    
}
