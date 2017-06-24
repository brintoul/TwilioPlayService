/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.controlledthinking.dropwizard.core.CustomerScheduledMessage;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class ScheduledMessageDAO extends AbstractDAO<CustomerScheduledMessage> {
    
    public ScheduledMessageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public CustomerScheduledMessage persist(CustomerScheduledMessage entity) throws HibernateException {
        return super.persist(entity);
    }
    
}
