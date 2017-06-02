package com.controlledthinking.dropwizard;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.controlledthinking.dropwizard.resources.AuthResource;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.db.MessageGroupDAO;
import com.controlledthinking.dropwizard.resources.PhoneNumberResource;
import com.controlledthinking.dropwizard.db.PhoneNumberDAO;
import com.controlledthinking.dropwizard.core.PhoneNumber;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.core.UserDTO;
import com.controlledthinking.dropwizard.db.UserDAO;
import com.controlledthinking.dropwizard.resources.CustomerResource;
import com.controlledthinking.dropwizard.resources.MessageGroupResource;
import com.controlledthinking.dropwizard.services.AwsQueueService;
import com.controlledthinking.dropwizard.services.QueueService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.stream.Stream;
import jwt4j.JWTHandler;
import jwt4j.JWTHandlerBuilder;

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
        return "banana-service";
    }

    @Override
    public void initialize(final Bootstrap<TwilioPhoneNumberConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }
    
    private JWTHandler<UserDTO> getJwtHandler(TwilioPhoneNumberConfiguration configuration)
    {
        return new JWTHandlerBuilder<UserDTO>()
                .withSecret(configuration.getAuthSalt().getBytes())
                .withDataClass(UserDTO.class)
                .build();
    }
    
    @Override
    public void run(final TwilioPhoneNumberConfiguration configuration,
                    final Environment environment) {
        final PhoneNumberDAO dao = new PhoneNumberDAO(hibernate.getSessionFactory());
        final CustomerDAO custDao = new CustomerDAO(hibernate.getSessionFactory());
        final MessageGroupDAO groupDao = new MessageGroupDAO(hibernate.getSessionFactory());
        final UserDAO userDao = new UserDAO(hibernate.getSessionFactory());        
        final JWTHandler<UserDTO> jwtHandler = getJwtHandler(configuration);
        
        //TODO:  THIS WILL HAVE TO FIND A NEW HOME.  PUT IT HERE FOR NOW.
        QueueService queueService = new AwsQueueService(configuration.getAwsConfiguration());
        queueService.setupQueue();
        
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        Stream.of(
                new AuthResource(jwtHandler, userDao),
                new PhoneNumberResource(dao),
                new CustomerResource(custDao, userDao),
                new MessageGroupResource(groupDao, custDao),
                new BananaAuthDynamicFeature(configuration, jwtHandler),
                new BananaAuthValueFactoryProvider.Binder())
            .forEach(jerseyEnvironment::register);
        
        SessionFactoryHealthCheck dbHealthCheck = new SessionFactoryHealthCheck(hibernate.getSessionFactory(), "SHOW TABLES FROM banana");
        environment.healthChecks().register("database", dbHealthCheck);
    }
}
