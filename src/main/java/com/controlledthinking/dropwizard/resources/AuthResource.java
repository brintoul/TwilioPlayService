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
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jwt4j.JWTHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author brintoul
 */
@Path("/auth")
public class AuthResource {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JWTHandler<UserDTO> jwtHandler;
    private final UserDAO userDao;
    
    public AuthResource(JWTHandler<UserDTO> jwtHandler, UserDAO userDao) {
        this.jwtHandler = jwtHandler;
        this.userDao = userDao;
    }
    
    @OPTIONS
    @Path("/login")
    public Response doIt() {
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
    
    @POST
    @Path("/login")
    @UnitOfWork
    @Produces("text/plain")
    public String login(@Valid UserCredentials userCredentials)
    {
        if(userCredentials == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        User theUser = userDao.fetchByUsername(userCredentials.getUsername());
	if(theUser == null) {
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        if(BCrypt.checkpw(userCredentials.getPassword(), theUser.getPassword())) {
            log.info("The user has authenticated.  Username was: %s", userCredentials.getUsername());
            //TODO:  RIGHT NOW WE ONLY USE THE FIRST PHONE NUMBER IN THE USER COLLECTION - GOTTA FIX
            String phoneNumberToUse = theUser.getPhoneNumbersCollection().iterator().next().getNumberText();
            UserDTO dtoUser = new UserDTO(theUser.getUserId(), theUser.getPrivileges(), phoneNumberToUse);
            return jwtHandler.encode(dtoUser);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
}
}
