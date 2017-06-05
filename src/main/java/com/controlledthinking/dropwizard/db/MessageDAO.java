/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.controlledthinking.dropwizard.core.CustomerImmediateMessage;
import com.controlledthinking.dropwizard.core.MessageGroup;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class MessageDAO extends AbstractDAO<CustomerImmediateMessage> {
    
    public MessageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public CustomerImmediateMessage persist(CustomerImmediateMessage entity) throws HibernateException {        
        return super.persist(entity); 
    }

}
