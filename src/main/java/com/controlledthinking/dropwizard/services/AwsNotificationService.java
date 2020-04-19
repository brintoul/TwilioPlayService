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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.logging.Level;

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
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static BananaAwsConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(BananaAwsConfiguration configuration) {
        AwsNotificationService.configuration = configuration;
    }

    public String getTopicArn() {
        log.info("The topic being sent to: " + topicArn);
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }
    
    public AwsNotificationService(BananaAwsConfiguration awsConfiguration) {
        log.info("The configuration is being set in the AwsNotificationService");
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
            log.info("Failed to deal with JSON", ex);
            return false;
        }
    }

    @Override
    public void setupOutgoingQueue(String queueName) {

        log.info("Entering setupOutgoingQueue");
        boolean createTopic = true;
        AmazonSNS snsClient = builder.build();
        ListTopicsResult ltresult = snsClient.listTopics(new ListTopicsRequest());
        List<Topic> topics = ltresult.getTopics();
        for( Topic topic : topics ) {
            if(topic.getTopicArn().endsWith(configuration.getQueueInfo().get(queueName))) {
                setTopicArn(topic.getTopicArn());
                createTopic = false;
            }
        }

        if( createTopic ) {
            log.info("Creating the topic and its ARN thang.");
            CreateTopicRequest ctr = new CreateTopicRequest(configuration.getQueueInfo().get(queueName));
            CreateTopicResult ctresp = snsClient.createTopic(ctr);
            setTopicArn(ctresp.getTopicArn());
        } else {
            log.info("Topic already created in SNS");
	}
    }
    
    @Override 
    public void setupIncomingQueue(String queueName) {
        log.info("Entering setupIncomingQueue");
        boolean createTopic = true;
        AmazonSNS snsClient = builder.build();
        ListTopicsResult ltresult = snsClient.listTopics(new ListTopicsRequest());
        List<Topic> topics = ltresult.getTopics();
        for( Topic topic : topics ) {
            if(topic.getTopicArn().endsWith(queueName)) {
                setTopicArn(topic.getTopicArn());
                createTopic = false;
            }
        }

        if( createTopic ) {
            log.info("Creating the topic and its ARN thang.");
            CreateTopicRequest ctr = new CreateTopicRequest(queueName);
            CreateTopicResult ctresp = snsClient.createTopic(ctr);
            setTopicArn(ctresp.getTopicArn());
        } else {
            log.info("Topic already created in SNS");
        }
    }
}
