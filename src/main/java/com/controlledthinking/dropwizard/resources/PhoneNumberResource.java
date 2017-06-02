/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.api.PhoneNumberRepresentation;
import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.db.PhoneNumberDAO;
import com.controlledthinking.dropwizard.core.PhoneNumber;
import com.controlledthinking.dropwizard.core.UserDTO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author brintoul
 */
@Path("/numbers")
@Produces(MediaType.APPLICATION_JSON)
public class PhoneNumberResource {

    private PhoneNumberDAO dao;
    
    public PhoneNumberResource(PhoneNumberDAO dao) {
        this.dao = dao;
    }
        
    @GET
    @Timed
    @UnitOfWork
    public PhoneNumberRepresentation retrieveNumbers(@AuthRequired(Privilege.USER) UserDTO user, @QueryParam("name") Optional<String> name) {
        return new PhoneNumberRepresentation(11, dao.findAll().get(0).getNumberText());
    }
     
    @GET
    @Timed
    @UnitOfWork
    @Path("/user")
    public List<PhoneNumber> retrieveNumbersForUser(@AuthRequired(Privilege.USER) UserDTO user) {
        System.out.println("The User: " + user.toString());
        return dao.findByUserId(user.getUserId());
    }
}
