/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.services;

import org.jvnet.hk2.annotations.Contract;


/**
 *
 * @author brintoul
 */
@Contract
public interface QueueService {
    public boolean sendMessageToQueue(Object message);
    public void setupOutgoingQueue(String queueName);
    public void setupIncomingQueue(String queueName);
}
