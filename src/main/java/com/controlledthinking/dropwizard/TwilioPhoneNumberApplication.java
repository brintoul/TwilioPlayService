package com.controlledthinking.dropwizard;

import com.controlledthinking.dropwizard.resources.PhoneNumberResource;
import com.controlledthinking.dropwizard.db.PhoneNumberDAO;
import com.controlledthinking.dropwizard.db.PhoneNumber;
import com.controlledthinking.dropwizard.db.User;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TwilioPhoneNumberApplication extends Application<TwilioPhoneNumberConfiguration> {

    private final HibernateBundle<TwilioPhoneNumberConfiguration> hibernate = new HibernateBundle<TwilioPhoneNumberConfiguration>(PhoneNumber.class, User.class) {
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
        final PhoneNumberResource phoneNumberResource = new PhoneNumberResource(dao);
        SessionFactoryHealthCheck dbHealthCheck = new SessionFactoryHealthCheck(hibernate.getSessionFactory(), "SHOW TABLES FROM banana");
        environment.healthChecks().register("database", dbHealthCheck);
        environment.jersey().register(phoneNumberResource);
    }
}
