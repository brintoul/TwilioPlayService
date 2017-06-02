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
public interface QueueService {
    public boolean sendMessageToQueue();
    public void setupQueue();
}
