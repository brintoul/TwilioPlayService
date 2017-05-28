package com.controlledthinking.dropwizard;

import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.resources.PhoneNumberResource;
import com.controlledthinking.dropwizard.db.PhoneNumberDAO;
import com.controlledthinking.dropwizard.core.PhoneNumber;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.db.UserDAO;
import com.controlledthinking.dropwizard.resources.CustomerResource;
import com.controlledthinking.dropwizard.resources.MessageGroupResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TwilioPhoneNumberApplication extends Application<TwilioPhoneNumberConfiguration> {

    private final HibernateBundle<TwilioPhoneNumberConfiguration> hibernate = new HibernateBundle<TwilioPhoneNumberConfiguration>(
            PhoneNumber.class, 
            User.class,
            Customer.class,
            MessageGroup.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(TwilioPhoneNumberConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new TwilioPhoneNumberApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(final Bootstrap<TwilioPhoneNumberConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final TwilioPhoneNumberConfiguration configuration,
                    final Environment environment) {
        final PhoneNumberDAO dao = new PhoneNumberDAO(hibernate.getSessionFactory());
        final CustomerDAO custDao = new CustomerDAO(hibernate.getSessionFactory());
        final MessageGroupDAO groupDao = new MessageGroupDAO(hibernate.getSessionFactory());
        final UserDAO userDao = new UserDAO(hibernate.getSessionFactory());
        
        final PhoneNumberResource phoneNumberResource = new PhoneNumberResource(dao);
        final CustomerResource customerResource = new CustomerResource(custDao, userDao);
        final MessageGroupResource groupResource = new MessageGroupResource(groupDao, custDao);
        
        SessionFactoryHealthCheck dbHealthCheck = new SessionFactoryHealthCheck(hibernate.getSessionFactory(), "SHOW TABLES FROM banana");
        environment.healthChecks().register("database", dbHealthCheck);
        environment.jersey().register(phoneNumberResource);
        environment.jersey().register(customerResource);
        environment.jersey().register(groupResource);
    }
}
