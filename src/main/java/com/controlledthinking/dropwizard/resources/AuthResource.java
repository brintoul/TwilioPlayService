/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.resources;

import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.beans.UserCredentials;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    
    private static final Map<UserCredentials, User> USERS = Stream.of(
            new SimpleEntry<>(new UserCredentials("brintoul", "temporary"),
                    new User("brintoul", new HashSet<>(Arrays.asList(Privilege.USER)))),
            new SimpleEntry<>(new UserCredentials("administrator", "tempag1an"),
                    new User("administrator", new HashSet<>(Arrays.asList(Privilege.values()))))
            ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    
    private final JWTHandler<User> jwtHandler;
    private final UserDAO userDao;
    
    public AuthResource(JWTHandler<User> jwtHandler, UserDAO userDao) {
        this.jwtHandler = jwtHandler;
        this.userDao = userDao;
    }
    
    @POST
    @Path("/login")
    @UnitOfWork
    //TODO:  THIS IS WORKING WITH PASSWORDS STORED AS PLAIN TEXT.  THIS WILL BE FIXED.
    public String login(@Valid UserCredentials userCredentials)
    {
        if(userDao.fetchUsernamePasswordHash(userCredentials.getUsername()).equals(userCredentials.getPassword()) ) {
            System.out.println("The user has authenticated");
            return jwtHandler.encode(USERS.get(userCredentials));
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
}
}
