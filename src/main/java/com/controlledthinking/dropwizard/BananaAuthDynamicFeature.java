/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard;

import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.core.UserDTO;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import jwt4j.JWTHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brintoul
 */
public class BananaAuthDynamicFeature implements DynamicFeature {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final TwilioPhoneNumberConfiguration configuration;
    private final JWTHandler<UserDTO> jwtHandler;

    public BananaAuthDynamicFeature(final TwilioPhoneNumberConfiguration configuration,
                              final JWTHandler<UserDTO> jwtHandler)
    {
        this.configuration = configuration;
        this.jwtHandler = jwtHandler;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext)
    {
        final Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod != null) {
            Stream.of(resourceMethod.getParameterAnnotations())
                    .flatMap(Arrays::stream)
                    .filter(annotation -> annotation.annotationType().equals(AuthRequired.class))
                    .map(AuthRequired.class::cast)
                    .findFirst()
                    .ifPresent(authRequired -> featureContext.register(getAuthFilter(authRequired.value())));
        }
    }

    private ContainerRequestFilter getAuthFilter(final Privilege[] requiredPrivileges)
    {
        return containerRequestContext -> {
            final String authHeader = containerRequestContext.getHeaderString(configuration.getAuthHeader());
            if (authHeader == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            String[] authParts = authHeader.split(" ");
            String jwtString = "";
            if( !authParts[0].trim().equals("Bearer")) {
                log.info("Bearer not found in auth header.");
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                jwtString = authParts[1].trim();
            }
            final UserDTO user;
            try {
                user = jwtHandler.decode(jwtString);
            } catch (Exception e) {
                log.info("Failed to decode JWT.  Exception: " + e.getMessage());
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            if (!user.getPrivileges().containsAll(Arrays.asList(requiredPrivileges))) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
            containerRequestContext.setProperty("user", user);
        };
    }
}
