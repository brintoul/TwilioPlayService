/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.controlledthinking.dropwizard.BananaAwsConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brintoul
 */
public class AwsNotificationService implements QueueService {
    
    private static BananaAwsConfiguration configuration;
    private static AWSStaticCredentialsProvider credentialsProvider;
    private static AwsClientBuilder.EndpointConfiguration epc;
    private static AmazonSNSClientBuilder builder;
    private String topicArn = "";


    public static BananaAwsConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(BananaAwsConfiguration configuration) {
        AwsNotificationService.configuration = configuration;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }
    
    public AwsNotificationService(BananaAwsConfiguration awsConfiguration) {
        AwsNotificationService.configuration = awsConfiguration;
        credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                configuration.getAccessKey(), 
                configuration.getSecretKey()));        
        epc = new AwsClientBuilder.EndpointConfiguration(configuration.getSnsRegionUrl(), 
                                                            configuration.getSnsRegion());
        builder = AmazonSNSClientBuilder.standard().withCredentials(credentialsProvider);
        builder.setEndpointConfiguration(epc);

    }

    @Override
    public boolean sendMessageToQueue(Object messageBody) {
        try {
            AmazonSNS snsClient = builder.build();
            PublishRequest pubReq = new PublishRequest();
            ObjectMapper mapperObj = new ObjectMapper();
            pubReq.setMessage(mapperObj.writeValueAsString(messageBody));
            pubReq.setTopicArn(this.getTopicArn());
            snsClient.publish(pubReq);
            return true;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AwsNotificationService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public void setupQueue() {

        boolean createTopic = true;
        AmazonSNS snsClient = builder.build();
        ListTopicsResult ltresult = snsClient.listTopics(new ListTopicsRequest());
        List<Topic> topics = ltresult.getTopics();
        for( Topic topic : topics ) {
            if(topic.getTopicArn().endsWith(configuration.getQueueInfo().get("immediateQueueName"))) {
                setTopicArn(topic.getTopicArn());
                createTopic = false;
            }
        }

        if( createTopic ) {
            CreateTopicRequest ctr = new CreateTopicRequest(configuration.getQueueInfo().get("immediateQueueName"));
            CreateTopicResult ctresp = snsClient.createTopic(ctr);
            setTopicArn(ctresp.getTopicArn());
        }
    }

}
