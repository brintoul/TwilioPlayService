/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.MessageGroup;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.Collection;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class CustomerDAO extends AbstractDAO<Customer> {
    
    public CustomerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public Customer create(Customer customer) {
        return persist(customer);
    }
    
    public Customer getById(int custId) {
        return get(custId);
    }
    
    public List<Customer> fetchForUser(int userId) {
        return list(namedQuery("Customer.findByUser").setParameter("userId", userId));
    }

    public List<Customer> fetchGroupCustomers(int groupId) {
        return list(namedQuery("Customer.findByGroup").setParameter("groupId", groupId));
    }

    public boolean deleteCustomer(int customerId) {
        Customer customer = get(customerId);
        Collection<MessageGroup> groups = customer.getMessageGroupCollection();
        groups.forEach((_item) -> {
            groups.remove(customer);
        });
        currentSession().remove(customer);
        return true;
    }
    
}
