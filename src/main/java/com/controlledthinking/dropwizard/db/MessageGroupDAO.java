/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class MessageGroupDAO extends AbstractDAO<MessageGroup> {
    
    public MessageGroupDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public MessageGroup persist(MessageGroup entity) throws HibernateException {
        return super.persist(entity); 
    }
    
    public List<MessageGroup> getAllForUser(int userId) {
        return list(namedQuery("MessageGroup.findByUserId").setParameter("userId", userId));
    }
    
    public List<Customer> getCustomersForGroup(int groupId) {
        return list(namedQuery("MessageGroup.findCustomersByGroupId").setParameter("groupId", groupId));
    }
    
    public MessageGroup getById(int groupId) {
        return get(groupId);
    }
    
}
