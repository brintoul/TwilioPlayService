/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard;

import com.controlledthinking.dropwizard.annotation.AuthRequired;
import com.controlledthinking.dropwizard.core.User;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

/**
 *
 * @author brintoul
 */
@Singleton
public class BananaAuthValueFactoryProvider extends AbstractValueFactoryProvider {

    @Inject
    public BananaAuthValueFactoryProvider(MultivaluedParameterExtractorProvider multivaluedParameterExtractorProvider,
                                    ServiceLocator serviceLocator)
    {
        super(multivaluedParameterExtractorProvider, serviceLocator, Parameter.Source.UNKNOWN);
    }

    @Override
    protected AbstractContainerRequestValueFactory<?> createValueFactory(Parameter parameter)
    {
        if (parameter.isAnnotationPresent(AuthRequired.class)) {
            return new AbstractContainerRequestValueFactory<User>()
            {
                @Override
                public User provide()
                {
                    final Object userObject = getContainerRequest().getProperty("user");
                    System.out.println("The USER OBJECT: " + userObject);
                    if (userObject != null && userObject instanceof User) {
                        return (User) userObject;
                    } else {
                        return null;
                    }
                }
            };
        } else {
            return null;
        }
    }

    private static class AuthRequiredInjectionResolver extends ParamInjectionResolver<AuthRequired>
    {
        public AuthRequiredInjectionResolver()
        {
            super(BananaAuthValueFactoryProvider.class);
        }
    }

    public static class Binder extends AbstractBinder
    {
        @Override
        protected void configure()
        {
            bind(BananaAuthValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(AuthRequiredInjectionResolver.class).to(new TypeLiteral<InjectionResolver<AuthRequired>>()
            {
            }).in(Singleton.class);
        }
    }
}
