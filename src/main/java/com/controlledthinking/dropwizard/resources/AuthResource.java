/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.controlledthinking.dropwizard.beans.UserCredentials;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.UserDAO;
import com.controlledthinking.dropwizard.services.AwsQueueService;
import com.controlledthinking.dropwizard.services.QueueService;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import jwt4j.JWTHandler;

/**
 *
 * @author brintoul
 */
@Path("/auth")
public class AuthResource {
    
    private final JWTHandler<UserDTO> jwtHandler;
    private final UserDAO userDao;
    
    public AuthResource(JWTHandler<UserDTO> jwtHandler, UserDAO userDao) {
        this.jwtHandler = jwtHandler;
        this.userDao = userDao;
    }
    
    @POST
    @Path("/login")
    @UnitOfWork
    //TODO:  THIS IS WORKING WITH PASSWORDS STORED AS PLAIN TEXT.  THIS WILL BE FIXED.
    public String login(@Valid UserCredentials userCredentials)
    {
        //QueueService aqs = new AwsQueueService();
        //aqs.setupQueue();
        
        User theUser = userDao.fetchByUsername(userCredentials.getUsername());
        if(theUser.getPassword().equals(userCredentials.getPassword()) ) {
            System.out.println("The user has authenticated");
            UserDTO dtoUser = new UserDTO(theUser.getUserId(), theUser.getPrivileges());
            return jwtHandler.encode(dtoUser);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
}
}
