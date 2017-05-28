/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.Set;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class UserDAO extends AbstractDAO<User> {
    
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public Set<Customer> fetchUserCustomers(int userId) {
        User theUser = uniqueResult(namedQuery("User.fetchAllCustomers").setParameter("userId", userId));
        return theUser.getCustomers();
    }
}
