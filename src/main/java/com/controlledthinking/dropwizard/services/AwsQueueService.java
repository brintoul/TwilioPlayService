/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.services;

/**
 *
 * @author brintoul
 */
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.controlledthinking.dropwizard.BananaAwsConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AwsQueueService implements QueueService {

    private static BananaAwsConfiguration configuration;
    private static AWSStaticCredentialsProvider credentialsProvider;
    private static AwsClientBuilder.EndpointConfiguration epc;
    private static AmazonSQSClientBuilder builder;
    private String queueUrl = "";
    private Logger log = Logger.getLogger(AwsQueueService.class.getName());

    public static BananaAwsConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(BananaAwsConfiguration configuration) {
        AwsQueueService.configuration = configuration;
    }

    public AwsQueueService(BananaAwsConfiguration awsConfiguration) {
        configuration = awsConfiguration;
        credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                configuration.getAccessKey(), 
                configuration.getSecretKey()));
        epc = new AwsClientBuilder.EndpointConfiguration(configuration.getSqsRegionUrl(), 
                                                            configuration.getSqsRegion());
        builder = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider);
        builder.setEndpointConfiguration(epc);
    }
    
    @Override
    public boolean sendMessageToQueue(Object messageBody) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();        
            AmazonSQS client = builder.build();
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.withQueueUrl(this.queueUrl);
            sendMessageRequest.withMessageBody(mapperObj.writeValueAsString(messageBody));
            client.sendMessage(sendMessageRequest);
            return true;
        } catch (JsonProcessingException ex) {
            log.severe(ex.toString());
            return false;
        }
    }
    
    @Override
    public void setupQueue() {
        
        AmazonSQS client = builder.build();
        ListQueuesRequest req = new ListQueuesRequest(configuration.getQueueInfo().get("immediateQueueName"));
        ListQueuesResult result = client.listQueues(req);
        if(result.getQueueUrls().isEmpty()) {
            //TODO:  ADD LOGGING - IN FACT, ADD LOGGING EVERYWHERE
            log.log(Level.INFO, "Creating queue for " + configuration.getQueueInfo().get("immediateQueueName"));
            CreateQueueRequest createQueueReq = new CreateQueueRequest();
            createQueueReq.setQueueName(configuration.getQueueInfo().get("immediateQueueName"));
            client.createQueue(createQueueReq);
        } else {
            if(result.getQueueUrls().size() > 1 ) {
                log.log(Level.WARNING, "More than one queue URL returned.");
            }
            this.queueUrl = result.getQueueUrls().get(0);        
        }
    }
    
}
