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
                sendMessageRequest.setMessageBody(mapperObj.writeValueAsString(messageBody));
            client.sendMessage(sendMessageRequest);
            return true;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AwsQueueService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public void setupQueue() {
        
        AmazonSQS client = builder.build();
        ListQueuesResult result = client.listQueues();
        if(result.getQueueUrls().isEmpty()) {
            //TODO:  ADD LOGGING - IN FACT, ADD LOGGING EVERYWHERE
            CreateQueueRequest createQueueReq = new CreateQueueRequest();
            createQueueReq.setQueueName(configuration.getQueueInfo().get("immediateQueueName"));
            client.createQueue(createQueueReq);
        } else {
            result.getQueueUrls().forEach(item -> System.out.println(item));
        }
    }
    
}
