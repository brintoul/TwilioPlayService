/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.beans;

import io.dropwizard.lifecycle.Managed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brintoul
 */
public class ScheduledMessageTask implements Managed {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ScheduledExecutorService scheduler;

    public ScheduledMessageTask() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void start() throws Exception {
        log.info("We are starting the timer task to check for new scheduled messages."); 
        scheduler.scheduleAtFixedRate(() -> { System.out.println("WOW!"); }, 8, 8, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        log.info("We are shutting down the timer task for scheduled messages.");
    }
    
}
