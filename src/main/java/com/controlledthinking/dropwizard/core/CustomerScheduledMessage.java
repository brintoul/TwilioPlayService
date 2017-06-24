/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author brintoul
 */
public class CustomerScheduledMessage extends Message {
    
    @JoinColumn(name="customer_id")
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;
    @Column(name = "time_to_send")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Calendar timeToSend;
    @Transient
    private String sendingNumberText;
    
    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPhoneNumberText() {
        return this.customer.getNumberText();
    }

    public void setPhoneNumberText(String phoneNumberText) {
        this.customer.setNumberText(phoneNumberText);
    }

    public String getSendingNumberText() {
        return sendingNumberText;
    }

    public void setSendingNumberText(String sendingNumberText) {
        this.sendingNumberText = sendingNumberText;
    }

    public java.util.Calendar getTimeToSend() {
        return timeToSend;
    }

    public void setTimeToSend(java.util.Calendar timeToSend) {
        this.timeToSend = timeToSend;
    }
    
}
