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
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    private final UserDAO userDao;

    public CustomerResource(CustomerDAO dao, UserDAO userDao) {
        this.dao = dao;
        this.userDao = userDao;
    }

    @PUT
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCustomer(@AuthRequired(Privilege.USER) UserDTO user, Customer customer) {
        customer.setUser(new User(user.getUserId()) );
        dao.create(customer);
        return "created";
    }
     
    @GET
    @Timed
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    public Set<Customer> fetchAllForUser(@AuthRequired(Privilege.USER) UserDTO user) {
        return userDao.fetchUserCustomers(user.getUserId());
    }
        
    @GET
    @Timed
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/group/{groupId}")
    public List<Customer> fetchAllForGroup(@AuthRequired(Privilege.USER) UserDTO user, @PathParam("groupId") int groupId) {
        return dao.fetchGroupCustomers(groupId);
    }
    
    @DELETE
    @Timed
    @UnitOfWork
    @Path("{customerId}")
    public boolean deleteCustomer(@AuthRequired(Privilege.USER) UserDTO user, @PathParam("customerId") int customerId) {
        return dao.deleteCustomer(customerId);
    }
}
