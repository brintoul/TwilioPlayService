/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.beans;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brintoul
 */
public class ScheduledMessageTask implements Managed {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void start() throws Exception {
        log.info("We are starting the timer task to check for new scheduled messages.");
    }

    @Override
    public void stop() throws Exception {
        log.info("We are shutting down the timer task for scheduled messages.");
    }
    
}
