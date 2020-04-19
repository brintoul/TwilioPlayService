/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author brintoul
 */
@Entity
@Table(name = "immediate_customer_message")
@XmlRootElement
@NamedQueries({})
public class CustomerImmediateMessage extends Message {
    
    @JoinColumn(name="customer_id")
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;
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
        //this.customer.setNumberText(phoneNumberText);
    }

    public String getSendingNumberText() {
        return sendingNumberText;
    }

    public void setSendingNumberText(String sendingNumberText) {
        this.sendingNumberText = sendingNumberText;
    }
}
